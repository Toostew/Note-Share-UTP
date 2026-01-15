package com.toostew.noteShare.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thumbnail")
public class ThumbnailController {
    //controller for thumbnail generation


    @GetMapping("/{category}")
    public ResponseEntity<Resource> view_thumbnail(@PathVariable String category){
        //id is a number from 1-4 that corresponds to a thumbnail
        Resource resource;
        if(category.equalsIgnoreCase("computing")){
            resource = new ClassPathResource("static/thumbnail/course/computing.jpg");
        }
        else if(category.equalsIgnoreCase("business management")){
            resource = new ClassPathResource("static/thumbnail/course/business.jpg");
        }
        else if(category.equalsIgnoreCase("science")){
            resource = new ClassPathResource("static/thumbnail/course/science.jpeg");
        }
        else {
            //engineering
            resource = new ClassPathResource("static/thumbnail/course/engineering.jpg");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

}
