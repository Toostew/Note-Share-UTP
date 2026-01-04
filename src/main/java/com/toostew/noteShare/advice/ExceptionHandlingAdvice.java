package com.toostew.noteShare.advice;


import com.toostew.noteShare.exception.pojo.awsSDKexceptions.R2ServiceException;
import com.toostew.noteShare.exception.pojo.other.PageControllerException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//this is a controller meant to display exceptions that occur during runtime
@ControllerAdvice
public class ExceptionHandlingAdvice {
    //TODO: ISSUE: The old, conventional way of handling every uncontrolled mapping is deprecated, you'll need to find another solution


    @ExceptionHandler(R2ServiceException.class)
    public String r2ServiceExceptionHandler(R2ServiceException ex){
        //there was an issue with the service
        System.out.println("There was an issue at R2Service");
        return "redirect:/";
    }


    @ExceptionHandler(PageControllerException.class)
    public String pageControllerExceptionHandler(PageControllerException ex){
        System.out.println("There was an issue at PageController");
        return "redirect:/";
    }


}
