package com.toostew.noteShare.controller;



import com.toostew.noteShare.entity.*;
import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.service.FileServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import com.toostew.noteShare.service.*;
import com.toostew.noteShare.service.auth.RegistrationService;
import com.toostew.noteShare.service.auth.UserService;
import com.toostew.noteShare.test.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PageController {

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${kafka.filescan.topic}")
    private String kafkaFilescanTopic;

    //front facing api

    private S3Client s3client;
    private R2Service r2Service;
    private FileService fileService;
    private StatisticsService statisticsService;
    private CourseService courseService;
    private UserService userService;
    private RegistrationService registrationService;
    private TestService testService;
    private TagService tagService;
    private KafkaTemplate<String, processRequest> kafkaTemplateThumbnailRequest;


    public PageController(S3Client s3client,R2Service r2Service,FileService fileService,StatisticsService statisticsService,
                          CourseService courseService, UserService userService,  RegistrationService registrationService,
                          TestService testService, TagService tagService, KafkaTemplate<String, processRequest> kafkaTemplateThumbnailRequest) {
        this.s3client = s3client;
        this.r2Service = r2Service;
        this.fileService = fileService;
        this.statisticsService = statisticsService;
        this.courseService = courseService;
        this.userService = userService;
        this.registrationService = registrationService;
        this.testService = testService;
        this.tagService = tagService;
        this.kafkaTemplateThumbnailRequest = kafkaTemplateThumbnailRequest;
    }

    @GetMapping("/")
    public String index(Model model){
        //returns the main menu
        return "main-menu";
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login-page";
    }


    //registration
    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }

    @PostMapping("/process-registration")
    public String processRegistration(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "email") String email,
                                      @RequestParam(name = "password") String password,
                                      @RequestParam(name = "confirmpassword") String confirmPassword,
                                      Model model,
                                      RedirectAttributes redirectAttributes){
        //process registration, expect registration form data as parameters
        //TODO: redirect to login and prefill the form with registration data OR automatically login user after sign up

        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()){
            redirectAttributes.addFlashAttribute("problem", "Please fill all the fields");
            return "redirect:/register";
        }
        else if (registrationService.usernameExists(username)){
            redirectAttributes.addFlashAttribute("problem", "That username already exists!");
            return "redirect:/register";
        }
        else if (!confirmPassword.equals(password)){
            redirectAttributes.addFlashAttribute("problem", "Passwords do not match!");
            return  "redirect:/register";
        }
        else if (registrationService.emailExists(email)){
            redirectAttributes.addFlashAttribute("problem", "That Student email already exists!");
            return  "redirect:/register";
        }

        //if no issues we can make the new User
        registrationService.registerNewUser(username, password, email);

        //return the user to the login page with the credentials automatically filled in
        redirectAttributes.addFlashAttribute("Ucredential", username);
        redirectAttributes.addFlashAttribute("Pcredential", password);


        return "redirect:/login";
    }

    //mapping for file verification, admin only
    @GetMapping("/file-verify")
    public String fileVerify(Authentication authentication, Model model){

        List<File_records> file_recordsList = fileService.getNumFile_Records(-1);
        List<File_records> resultList = fileService.filterOnlyNonViewableRecords(file_recordsList);
        model.addAttribute("filteredList", resultList);

        return "auth/verification-panel";
    }

    @PostMapping("/file-verify/process/accept")
    public String fileVerifyAcceptProcess(@RequestParam(name = "accept") int id,Authentication authentication, Model model){
        File_records temp = fileService.getFile_recordById(id);
        temp.setViewable(true);
        fileService.updateFile_record(temp);

        return "redirect:/file-verify";
    }

    @PostMapping("/file-verify/process/delete")
    public String fileVerifyDeleteProcess(@RequestParam(name = "delete") int id,Authentication authentication, Model model){
        //delete record and associated object
        try{
            File_records temp = fileService.getFile_recordById(id);
            r2Service.deleteObjectWithBucketAndKey(temp.getStorage_path(), temp.getStored_name());
            fileService.deleteFile_record(temp.getId());
            return "redirect:/file-verify";
        } catch(R2ServiceException e){
            throw new PageControllerException("Issue in Page Controller, could not delete file",e);
        } catch(FileServiceException e){
            throw new  PageControllerException("Issue in Page Controller, could not delete file",e);
        } catch (Exception e){
            throw new  PageControllerException("Issue in Page Controller, could not delete file",e);
        }

    }

    @GetMapping("/access-denied")
    public String accessDenied(Authentication authentication, Model model){
        System.out.println("Attempted access to denied resources");
        System.out.println("User: " +  authentication.getPrincipal());
        System.out.println("Details: " +  authentication.getDetails());
        System.out.println("Authority: " +  authentication.getAuthorities());
        return "auth/access-denied";
    }

    @GetMapping("/upload")
    public String test(Model model){
        //provide list of courses to select
        List<Course> courseList = courseService.getAllCourses();
        model.addAttribute("courses", courseList);

        return "upload";
    }

    //Physically looks for a parameter called file, hence, the form input name must be called file as well.
    //this method is no longer used and has been replaced with method that supports multiple file uploads
    /*
    public String fileReceived(@RequestParam(name = "file") MultipartFile file,
                               @RequestParam(name = "courseId") int courseId,
                               @RequestParam(name = "tags") String tagsListString,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication){

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



        //create user
        User user =  userService.getUserByUsername(authentication.getName());



        LocalDate date_created = LocalDate.now();

        File_records temp = new File_records();
        temp.setOriginal_name(original_name);
        temp.setStored_name(stored_name);
        temp.setContent_type(content_type);
        temp.setSize(size);
        temp.setStorage_path(storage_path);
        temp.setUser(user); //set owner
        temp.setCourse(courseService.getCourse(courseId)); //set course by selection
        temp.setDate_created(date_created);
        temp.setViewable(false);


        //parse tags, create them if they havent existed yet
        List<Tags> tagsList = tagService.createTagListFromString(tagsListString);
        for(Tags tag : tagsList){
            tagService.createTag(tag);
        }
        temp.setTags(tagsList);





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
        //recapture id for use in thumbnail request
        File_records recapture = fileService.createFile_record(temp);

        //create a ThumbnailRequest
        ThumbnailRequest thumbnailRequest = new ThumbnailRequest();
        thumbnailRequest.setFile_records_id(recapture.getId());
        thumbnailRequest.setContent_type(content_type);
        thumbnailRequest.setStored_name(temp.getStored_name());

        //create a thumbnailRequest message
        kafkaTemplateThumbnailRequest.send(kafkaTopic,String.valueOf(thumbnailRequest.getFile_records_id()) ,thumbnailRequest)
                .whenComplete((res, e) -> {
                    if(e == null){
                        System.out.println("Sending kafka message with thumbnail request: " + thumbnailRequest.toString());
                    } else {
                        System.out.println("An unknown issue occured " + e.getMessage());
                    }
                });

        return "redirect:/upload";
    } */


    //alternative method for handling multiple files
    @PostMapping("/fileReceived")
    public String fileReceivedMultiple(@RequestParam(name = "file") MultipartFile[] files,
                                       @RequestParam(name = "courseId") int courseId,
                                       @RequestParam(name = "tags") String tagsListString,
                                       Model model,
                                       RedirectAttributes redirectAttributes,
                                       Authentication authentication){

        //parse tags, create them if they havent existed yet
        List<Tags> tagsList = tagService.createTagListFromString(tagsListString);
        for(Tags tag : tagsList){
            tagService.createTag(tag);
        }

        //perform for EACH file!
        for(MultipartFile file : files){

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

            //create user
            User user =  userService.getUserByUsername(authentication.getName());



            LocalDate date_created = LocalDate.now();

            File_records temp = new File_records();
            temp.setOriginal_name(original_name);
            temp.setStored_name(stored_name);
            temp.setContent_type(content_type);
            temp.setSize(size);
            temp.setStorage_path(storage_path);
            temp.setUser(user); //set owner
            temp.setCourse(courseService.getCourse(courseId)); //set course by selection
            temp.setDate_created(date_created);
            temp.setViewable(false);
            temp.setVerified("UNVERIFIED");


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
            //recapture id for use in thumbnail request
            File_records recapture = fileService.createFile_record(temp);

            //create a ThumbnailRequest
            processRequest processRequest = new processRequest();
            processRequest.setFile_records_id(recapture.getId());
            processRequest.setContent_type(content_type);
            processRequest.setStored_name(temp.getStored_name());

            //create a thumbnailRequest message
            kafkaTemplateThumbnailRequest.send(kafkaTopic,String.valueOf(processRequest.getFile_records_id()) ,processRequest)
                    .whenComplete((res, e) -> {
                        if(e == null){
                            System.out.println("Sending kafka message with thumbnail request: " + processRequest.toString());
                        } else {
                            throw new PageControllerException("PageController: An unknown issue occured attempting to send file thumbnail request ", e);
                        }
                    });


            kafkaTemplateThumbnailRequest.send(kafkaFilescanTopic,String.valueOf(processRequest.getFile_records_id()) ,processRequest)
                    .whenComplete((res, e) -> {
                        if(e == null){
                            System.out.println("Sending kafka message with file scanning request: " + processRequest.toString());
                        } else {
                            throw new  PageControllerException("PageController: An unknown issue occured attempting to send file verification request ", e);
                        }
                    });


        }

        return "redirect:/upload";
    }


    //list every single object in "islands"
    //only files that have viewable == 1
    @GetMapping("/view-all")
    public String view(Model model){
        //for now we want to just view all of the items so -1
        List<File_records> temp = fileService.getNumFile_Records(-1);
        List<File_records> list = fileService.filterOnlyViewableRecords(temp);
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

        List<File_records> temp = courseService.getFile_recordsList(courseId);
        List<File_records> filteredList = fileService.filterOnlyViewableRecords(temp);

        model.addAttribute("fileRecordsInCourse", filteredList);
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


    //render specific file
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

    @GetMapping("/file/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable int id, Model model) throws PageControllerException{
        File_records temp = fileService.getFile_recordById(id);
        String bucket = temp.getStorage_path();
        String key = temp.getStored_name();
        Resource payload =  r2Service.getObjectWithBucketAndKey(temp.getStorage_path(),temp.getStored_name()).getBody();


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(temp.getContent_type()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + temp.getOriginal_name() + "\"")
                .body(payload);

    }


    //returns a HTTP response used to view images
    @GetMapping("/render/{id}")
    @ResponseBody //normally a getMapping would try to resolve to a file with the same name as the return
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
