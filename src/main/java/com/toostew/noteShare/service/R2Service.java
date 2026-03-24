package com.toostew.noteShare.service;

import com.toostew.noteShare.entity.ProcessRequest;
import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


//service to dirrectly query Cloudflare's R2
@Service
public class R2Service {

    private S3Client s3client;
    private StatisticsService statisticsService;
    private KafkaTemplate<String, ProcessRequest> kafkaTemplate;


    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${kafka.filescan.topic}")
    private String kafkaFilescanTopic;

    public R2Service(S3Client s3client,StatisticsService statisticsService,
                     KafkaTemplate<String, ProcessRequest> kafkaTemplate) {
        this.s3client = s3client; //s3client is the class that handles all the r2 operations. think of it as a DAO
        this.statisticsService = statisticsService;
        this.kafkaTemplate = kafkaTemplate;
    }


    //getObjectWithBucketAndName
    //return in the form of a responseEntity
    public ResponseEntity<Resource> getObjectWithBucketAndKey(String bucket, String key){


        try{
            //get object via stream
            //ResponseInputStream is an implementation of InputStream,
            //incoming data does not arrive all at once, we need to collect it, like running water into a bail
            //we "pipe it along" once our bail is full (we have the full file in bytes)
            //we never convert it or anything, we just hold it's raw bytecode, what it is and what to do with it is
            //dictated by headers of our output, if we say to treat it like a png, it'll be treated like one (even if its not)
            //InputStreams happens once per call and we can only use it once,
            ResponseInputStream<GetObjectResponse> responseInputStream = s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            );
            //return object metadata, we need the filetype
            GetObjectResponse response = responseInputStream.response();
            String contentType = response.contentType(); //we now have the retrieved item's datatype
            long size = response.contentLength();

            //InputStreamResource is a Resource implementation of InputStream
            //we can fill the body of a http response with a resource, so we must make a resource
            //we have an inputStream (ResourceInputStream), we can use InputStreamResource to convert an inputStream into a valid resource
            //we can also pass a description for later
            InputStreamResource resource = new InputStreamResource(responseInputStream,"description");

            //if everything is alright, we increment the object transactions
            statisticsService.incrementObjectTrasactions();
            statisticsService.incrementEgressVolume(size);

            //here we build a http response
            return ResponseEntity.ok()
                    .contentType(contentTypeDetect(contentType))
                    .body(resource);
            //we do not need to specify .build() after placing a .body()

        } catch (AwsServiceException e) {
            //R2AWS Client issue
            throw new R2ServiceException("Issue with Get Object at R2Service layer, R2 Server issue",e);
        } catch (SdkClientException e) {
            //SDK client side issue
            throw new R2ServiceException("Issue with Get Object at R2Service layer, client issue",e);
        }



    }


    //convert multipart to inputStream
    //deprecated, MultipartFile already has a built in getInputStream()
    /*
    public InputStream processMultiPartFile(MultipartFile multipartFile){
        try{
            //we can only really use these files meaningfully while they are in an inputstream
            InputStream inputStream = multipartFile.getInputStream();
            return inputStream;
        } catch (IOException e){
            throw new R2ServiceException("Issue with processing Multipart File at R2Service layer",e);
        }
    }
    */

    //@Async will make this run as a background thread and is non-blocking (as it completes it does not
    //prevent code from running)
    //because it is async we must also handle the kafka messaging because we need this to be done sequentially,
    //as in, Object must be in R2 before the Thumbnail and file scanner services can work
    @Async
    public void postObjectWithBucketAndKey(String bucket, String key, byte[] item, long size, String contentType, ProcessRequest processRequest){
        try(InputStream inputStream = new ByteArrayInputStream(item)){

            s3client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(inputStream,size)
                    //the size MUST be precise, else we risk failed uploads
            );
            //if there is no issue, increment the statistics
            statisticsService.incrementEgressVolume(size);
            statisticsService.incrementObjectTrasactions();

            //create a thumbnailRequest message
            kafkaTemplate.send(kafkaTopic,String.valueOf(processRequest.getFile_records_id()) ,processRequest)
                    .whenComplete((res, e) -> {
                        if(e == null){
                            System.out.println("Sending kafka message with thumbnail request: " + processRequest.toString());
                        } else {
                            throw new PageControllerException("PageController: An unknown issue occured attempting to send file thumbnail request ", e);
                        }
                    });


            kafkaTemplate.send(kafkaFilescanTopic,String.valueOf(processRequest.getFile_records_id()) ,processRequest)
                    .whenComplete((res, e) -> {
                        if(e == null){
                            System.out.println("Sending kafka message with file scanning request: " + processRequest.toString());
                        } else {
                            throw new  PageControllerException("PageController: An unknown issue occured attempting to send file verification request ", e);
                        }
                    });
        } catch (AwsServiceException e){
            throw new R2ServiceException("Issue with Post Object at R2Service layer, R2 Server issue",e);
        } catch (SdkClientException e){
            throw new R2ServiceException("Issue with Post Object at R2Service layer, client issue",e);
        } catch (IOException e){
            throw new R2ServiceException("Issue with Post Object at R2Service layer, client issue",e);
        }

    }
    //delete the object in question, this should only be done in tandem with removing the appropriate File_records
    public void deleteObjectWithBucketAndKey(String bucket, String key){
        try{
            s3client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch(AwsServiceException e){
            throw new R2ServiceException("Issue with Delete Object at R2Service layer, R2 Server issue",e);
        } catch (SdkClientException e){
            throw new R2ServiceException("Issue with Delete Object at R2Service layer, client issue",e);
        }
    }

    //when getting an object from R2, we are crafting a HTTP response, and in it, we need to specify a contentType
    //contentType only accepts it in the format of Mediatype, so we parse the String ContentType through here to get a mediaType match
    private MediaType contentTypeDetect(String contentType){
        //images
        if(contentType == null){
            return null;
        }
        else if(contentType.equals("image/jpeg") || contentType.equals("image/jpg")){
            return MediaType.IMAGE_JPEG;
        }
        else if(contentType.equals("image/png")){
            return MediaType.IMAGE_PNG;
        }
        else if(contentType.equals("image/gif")){
            return MediaType.IMAGE_GIF;
        }


        //pdf
        else if(contentType.equals("application/pdf")){
            return MediaType.APPLICATION_PDF;
        }

        return null;

    }





}
