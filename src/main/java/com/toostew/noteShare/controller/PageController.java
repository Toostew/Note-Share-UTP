package com.toostew.noteShare.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/page")
public class PageController {
    //front facing api

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

}
