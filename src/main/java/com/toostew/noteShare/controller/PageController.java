package com.toostew.noteShare.controller;



import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.FileServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import com.toostew.noteShare.service.FileService;
import com.toostew.noteShare.service.R2Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Controller
public class PageController {
    //front facing api

    private S3Client s3client;
    private R2Service r2Service;
    private FileService fileService;

    public PageController(S3Client s3client,R2Service r2Service,FileService fileService) {
        this.s3client = s3client;
        this.r2Service = r2Service;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String index(Model model){
        //returns the main menu
        return "index";
    }






    @GetMapping("/upload")
    public String test(Model model){
        //this will test the front facing controller for file uploads
        return "file-share";
    }


    @PostMapping("/fileReceived")
    public String fileReceived(@RequestParam(name = "file") MultipartFile file){

        //create a File_record to store metadata
        String original_name = file.getOriginalFilename();
        String stored_name = fileService.createNewFileRecordStoredName(); //this generates with UUID-LOCALDATE
        String content_type = file.getContentType();
        long size = file.getSize();
        String storage_path = "first-storage";
        int owner_id = 0;
        LocalDate date_created = LocalDate.now();

        File_records temp = new File_records();
        temp.setOriginal_name(original_name);
        temp.setStored_name(stored_name);
        temp.setContent_type(content_type);
        temp.setSize(size);
        temp.setStorage_path(storage_path);
        temp.setOwner_id(owner_id);
        temp.setDate_created(date_created);

        //convertMultipartFile into inputStream
        try{
            InputStream inputStream = file.getInputStream();
            //send inputStream to object storage
            r2Service.postObjectWithBucketAndKey(storage_path,stored_name,inputStream,size,content_type);
            System.out.println("attempting to communicate with R2");

        } catch(IOException e){
            //issue with converting into inputstream
            throw new PageControllerException("Issue in PageController, issue getting inputStream from MultiPartFile",e);
        } catch(R2ServiceException e){
            throw new PageControllerException("Issue in PageController, couldn't upload to R2 Service",e);
        }

        //submission to fileRecords only happens if no exception is returned to prevent false entries
        fileService.createFile_record(temp);

        return "redirect:/upload";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model){
        try {
            File_records temp = fileService.getFile_recordById(id);
            model.addAttribute("file_record",temp);
            model.addAttribute("id",id);
            return "render";
        }catch(FileServiceException e){
            throw new PageControllerException("Issue in PageController, Couldn't retrieve file metadata from database",e);
        }
    }


    //for testing, renders image from database using id
    @GetMapping("/render/{id}")
    public ResponseEntity<Resource> render(@PathVariable int id){
        try{
            File_records temp = fileService.getFile_recordById(id);
            String bucket = temp.getStorage_path();
            String key = temp.getStored_name();
            ResponseEntity<Resource> response = r2Service.getObjectWithBucketAndKey(bucket,key);
            return response;
        } catch(FileServiceException e){
            throw new PageControllerException("Issue in PageController, Couldn't retrieve file metadata from database",e);
        } catch(R2ServiceException e){
            //R2 Service issue
            throw new PageControllerException("Issue in PageController, Couldn't retrieve object from R2",e);
        }

    }

    //for testing delete operation
    @GetMapping("/delete/{id}")
    public String deleteObjectAndRecord(@PathVariable int id){
        //get the record, and delete it's associated object

        try{
            System.out.println("Attempting to delete file of id: "+id);
            File_records temp = fileService.getFile_recordById(id);
            String bucket = temp.getStorage_path();
            String key = temp.getStored_name();
            r2Service.deleteObjectWithBucketAndKey(bucket,key);
            System.out.println("deleted file of id: "+id);
        } catch(R2ServiceException e){
            throw new PageControllerException("Issue in PageController, Issue with Deleting Object with Bucket and Key",e);
        } catch(FileServiceException e){
            throw new PageControllerException("Issue in PageController, Issue with getting file by ID",e);
        }
        return "redirect:/page/upload";
    }










}
