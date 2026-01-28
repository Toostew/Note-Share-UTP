package com.toostew.noteShare.controller;



import com.toostew.noteShare.entity.*;
import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.FileServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import com.toostew.noteShare.service.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.annotations.NotNull;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PageController {
    //front facing api

    private S3Client s3client;
    private R2Service r2Service;
    private FileService fileService;
    private StatisticsService statisticsService;
    private CourseService courseService;

    public PageController(S3Client s3client,R2Service r2Service,FileService fileService,StatisticsService statisticsService,CourseService courseService) {
        this.s3client = s3client;
        this.r2Service = r2Service;
        this.fileService = fileService;
        this.statisticsService = statisticsService;
        this.courseService = courseService;
    }

    @GetMapping("/")
    public String index(Model model){
        //returns the main menu
        return "main-menu";
    }


    @GetMapping("/upload")
    public String test(Model model){
        //provide list of courses to select
        List<Course> courseList = courseService.getAllCourses();
        model.addAttribute("courses", courseList);

        return "upload";
    }

    //Physically looks for a parameter called file, hence, the form input name must be called file as well.
    @PostMapping("/fileReceived")
    public String fileReceived(@RequestParam(name = "file") MultipartFile file,
                               @RequestParam(name = "courseId") int courseId,
                               Model model,
                               RedirectAttributes redirectAttributes){

        //we need to check if the file is empty, if it is, redirect back to upload but with the model
        //also check if the file exceeds 20MB
        if(file.isEmpty() || file.getSize() == 0){
            redirectAttributes.addFlashAttribute("error", "Please select a file");
            return "redirect:/upload";
        } else if(file.getSize() > 2e+7){
            redirectAttributes.addFlashAttribute("error", "File size exceeds 20MB");
            return "redirect:/upload";
        }

        //create a File_record to store metadata
        String original_name = file.getOriginalFilename();
        String stored_name = fileService.createNewFileRecordStoredName(); //this generates with UUID-LOCALDATE
        String content_type = file.getContentType();
        long size = file.getSize();
        String storage_path = "first-storage";

        //debugging only, hardcoded owner stats
        User tempUser = new User();
        tempUser.setId(1);
        tempUser.setPassword("{noop}1234");
        tempUser.setUsername("admin");



        LocalDate date_created = LocalDate.now();

        File_records temp = new File_records();
        temp.setOriginal_name(original_name);
        temp.setStored_name(stored_name);
        temp.setContent_type(content_type);
        temp.setSize(size);
        temp.setStorage_path(storage_path);
        temp.setUser(tempUser); //set temp owner
        temp.setCourse(courseService.getCourse(courseId)); //set course by selection
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

    //list every single object in "islands"
    @GetMapping("/view-all")
    public String view(Model model){
        //for now we want to just view all of the items so -1
        List<File_records> list = fileService.getNumFile_Records(-1);
        model.addAttribute("ObjectList", list);
        return "view-all";
    }

    //view categories
    @GetMapping("/view-categories")
    public String view_categories(Model model){
        List<Course> courseList = courseService.getAllCourses();
        model.addAttribute("courses", courseList);

        return "view-categories";
    }

    @GetMapping("/view-category/{courseId}")
    public String view_category(@PathVariable("courseId") int courseId, Model model){

        model.addAttribute("fileRecordsInCourse", courseService.getFile_recordsList(courseId));
        model.addAttribute("course", courseService.getCourse(courseId)); //attribute: Associated course

        return "view-category-withID";
    }



    @GetMapping("/statistics")
    public String statistics(Model model){
        Statistics statistics = statisticsService.getStatistics();
        double statisticsInMegaBytes = Math.round((double)statistics.getEgress_volume()/1000000);
        model.addAttribute("statistics", statistics);
        model.addAttribute("egressInMegaBytes", statisticsInMegaBytes);
        return "statistics";
    }


    @GetMapping("/view/{id}")
    public String viewWithId(@PathVariable int id, Model model){
        try {
            File_records temp = fileService.getFile_recordById(id);
            model.addAttribute("file_record",temp);
            model.addAttribute("id",id);
            return "render"; //do not be confused with the mapping render and the html render
        } catch(FileServiceException e){
            throw new PageControllerException("Issue in PageController, Couldn't retrieve file metadata from database",e);
        }
    }


    //returns a HTTP response used to view images
    @GetMapping("/render/{id}")
    @ResponseBody
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

    //handling downloads
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable int id){
        try{
            File_records temp = fileService.getFile_recordById(id);
            Resource tempPayload =  r2Service.getObjectWithBucketAndKey(temp.getStorage_path(),temp.getStored_name()).getBody();

            //we need the file type and the original name
            String originalName = temp.getOriginal_name();
            String fileType = temp.getContent_type();



            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + originalName + "\"")
                    .body(tempPayload);

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
            System.out.println("deleted file of id: "+id+" from object storage"); //deleted in R2 Cloudflare
            //delete File_record
            fileService.deleteFile_record(id);
            System.out.println("deleted File_record of id: "+id);

        } catch(R2ServiceException e){
            throw new PageControllerException("Issue in PageController, Issue with Deleting Object with Bucket and Key",e);
        } catch(FileServiceException e){
            throw new PageControllerException("Issue in PageController, Issue with getting file by ID",e);
        }
        return "redirect:/";
    }






}
