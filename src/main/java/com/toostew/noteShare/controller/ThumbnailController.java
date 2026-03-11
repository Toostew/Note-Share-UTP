package com.toostew.noteShare.controller;

import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Thumbnail;
import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import com.toostew.noteShare.exception.pojo.other.ThumbnailControllerException;
import com.toostew.noteShare.exception.pojo.service.FileServiceException;
import com.toostew.noteShare.service.FileService;
import com.toostew.noteShare.service.R2Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/thumbnail")
public class ThumbnailController {
    //controller for thumbnail generation, for view-categories

    @Value("${Thumbnail.Bucket.Name}")
    private String bucketName;

    private R2Service r2Service;
    private FileService fileService;

    public ThumbnailController(R2Service r2Service, FileService fileService) {
        this.r2Service = r2Service;
        this.fileService = fileService;
    }


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

    //returns a HTTP response used to view images
    @GetMapping("/render/{id}")
    @ResponseBody
    public ResponseEntity<Resource> render(@PathVariable int id){
        try{
            File_records temp = fileService.getFile_recordById(id);
            Thumbnail thumbnail = temp.getSingleThumbnail();
            String key = thumbnail.getStored_name();
            ResponseEntity<Resource> response = r2Service.getObjectWithBucketAndKey(bucketName,key);
            return response;
        } catch(FileServiceException e){
            throw new ThumbnailControllerException("Issue in ThumbnailController, Couldn't retrieve file metadata from database",e);
        } catch(R2ServiceException e){
            //R2 Service issue
            throw new ThumbnailControllerException("Issue in ThumbnailController, Couldn't retrieve object from R2",e);
        }

    }

}
