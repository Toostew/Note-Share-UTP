package com.toostew.noteShare.controller;



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

@Controller
@RequestMapping("/page")
public class PageController {
    //front facing api

    private S3Client s3client;

    public PageController(S3Client s3client){
        this.s3client = s3client;
    }

    @GetMapping("/test")
    public String test(Model model){
        //this will test the front facing controller for file uploads
        return "file-share";
    }

    @PostMapping("/fileReceived")
    public String fileReceived(@RequestParam(name = "file") MultipartFile file){
        //test to see if files are properly received
        return "file-received";
    }

    @GetMapping("/view")
    public String view(Model model){
        return "render";
    }


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



}
