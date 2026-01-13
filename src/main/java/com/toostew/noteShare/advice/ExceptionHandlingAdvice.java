package com.toostew.noteShare.advice;


import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//this is a controller meant to display exceptions that occur during runtime
@ControllerAdvice
public class ExceptionHandlingAdvice {
    //TODO: ISSUE: The old, conventional way of handling every uncontrolled mapping is deprecated, you'll need to find another solution


    @ExceptionHandler(R2ServiceException.class)
    public String r2ServiceExceptionHandler(R2ServiceException ex, Model model){
        //there was an issue with the service
        ex.printStackTrace();
        model.addAttribute("issueMessage", "There was an issue communicating with our storage service");
        return "error/error";
    }


    @ExceptionHandler(PageControllerException.class)
    public String pageControllerExceptionHandler(PageControllerException ex, Model model){
        ex.printStackTrace();
        model.addAttribute("issueMessage", "There was an issue within our servers, please try again later");
        return "error/error";
    }


    @ExceptionHandler(Exception.class)
    public String genericExceptionHandler(Exception ex, Model model){
        ex.printStackTrace();
        model.addAttribute("issueMessage", "An unepected error occurred, please try again later");
        return "error/error";
    }




}
