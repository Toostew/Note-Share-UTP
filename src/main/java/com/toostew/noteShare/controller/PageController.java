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
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/upload")
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

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model){
        File_records temp = fileService.getFile_recordById(id);
        model.addAttribute("file_record",temp);
        model.addAttribute("id",id);
        return "render";
    }




    //for testing, renders image from database from id
    @GetMapping("/render/{id}")
    public ResponseEntity<Resource> render(@PathVariable int id){
        File_records temp = fileService.getFile_recordById(id);
        String bucket = temp.getStorage_path();
        String key = temp.getStored_name();
        ResponseEntity<Resource> response = r2Service.getObjectWithBucketAndKey(bucket,key);
        return response;
    }


}
