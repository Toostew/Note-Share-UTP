package com.toostew.noteShare.controller;



import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.service.FileService;
import com.toostew.noteShare.service.R2Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.io.InputStream;
import java.time.LocalDate;

@Controller
@RequestMapping("/page")
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

    @GetMapping("/test")
    public String test(Model model){
        //this will test the front facing controller for file uploads
        return "file-share";
    }

    @PostMapping("/fileReceived")
    public String fileReceived(@RequestParam(name = "file") MultipartFile file){

        //create a File_record to store metadata
        String original_name = file.getOriginalFilename();
        String stored_name = fileService.createNewFileRecordStoredName();
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

        //submit the File_records
        fileService.createFile_record(temp);

        //convertMultipartFile into inputStream
        InputStream inputStream = r2Service.processMultiPartFile(file);

        //send inputStream to object storage
        r2Service.postObjectWithBucketAndKey(storage_path,stored_name,inputStream,size,content_type);
        System.out.println("attempting to communicate with R2");


        return "redirect:/page/test";
    }

    @GetMapping("/view")
    public String view(Model model){
        return "render";
    }




    //for testing, deprecated
    /*
    @GetMapping("/render")
    public ResponseEntity<Resource> render(Model model){


        //getObject takes GetObjectRequest as an Argument
        //we must provide GetObjectRequest with the bucket name and full key

        ResponseInputStream<GetObjectResponse> stream =
            s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket("first-storage")
                            .key("ThePalouse-1296-3200x2133.jpg")
                    .build()
            );

        InputStreamResource resource = new InputStreamResource(stream);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
    */


}
