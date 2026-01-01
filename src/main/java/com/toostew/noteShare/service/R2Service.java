package com.toostew.noteShare.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;


//service to dirrectly query Cloudflare's R2
@Service
public class R2Service {

    private S3Client s3client;

    public R2Service(S3Client s3client) {
        this.s3client = s3client; //s3client is the class that handles all the r2 operations. think of it as a DAO
    }


    //getObjectWithBucketAndName
    //return in the form of a responseEntity
    public ResponseEntity<Resource> getObjectWithBucketAndKey(String bucket, String key){

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



        //InputStreamResource is a Resource implementation of InputStream
        //we can fill the body of a http response with a resource, so we must make a resource
        //we have an inputStream (ResourceInputStream), we can use InputStreamResource to convert an inputStream into a valid resource
        //we can also pass a description for later
        InputStreamResource resource = new InputStreamResource(responseInputStream,"description");


        //here we build a http response
        return ResponseEntity.ok()
                .contentType(contentTypeDetect(contentType))
                .body(resource);
                //we do not need to specify .build() after placing a .body()

    }


    //convert multipart to inputStream
    public InputStream processMultiPartFile(MultipartFile multipartFile){
        try{
            //we can only really use these files meaningfully while they are in an inputstream
            InputStream inputStream = multipartFile.getInputStream();
            return inputStream;
        } catch (IOException e){
            e.printStackTrace();
            return null; //in case of an error
        }
    }

    public void postObjectWithBucketAndKey(String bucket, String key, InputStream inputStream, long size, String contentType){
        s3client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build(),
                RequestBody.fromInputStream(inputStream,size)
                //the size MUST be precise, else we risk failed uploads
        );
    }


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
