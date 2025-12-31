package com.toostew.noteShare.RESTController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

@RestController
@RequestMapping("/r2")
public class R2RESTController {

    private S3Client s3client;


    public R2RESTController(S3Client s3client) {
        this.s3client = s3client; //s3client is the class that handles all the r2 operations. think of it as a DAO

    }

    @GetMapping("/test")
    public String test(){
        s3client.listObjectsV2(builder -> builder.bucket("first-storage"));
        return "if you are reading this, test successful";
    }



}
