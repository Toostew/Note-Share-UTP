package com.toostew.noteShare.RESTController;


import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

//this is a test class to test the api for the file_records service
@RestController
@RequestMapping("/api")
public class File_RecordsRESTController {

    private FileService fileService;
    private JsonMapper jsonMapper;



    public File_RecordsRESTController(FileService fileService, JsonMapper jsonMapper) {
        this.fileService = fileService;
        this.jsonMapper = jsonMapper;
    }



    @GetMapping("/{fileid}")
    public File_records getFile_record(@PathVariable int fileid){
        return fileService.getFile_recordById(fileid);
    }


}
