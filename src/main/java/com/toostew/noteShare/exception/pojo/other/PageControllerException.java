package com.toostew.noteShare.exception.pojo.other;

//exception occurs at the PageController level
public class PageControllerException extends RuntimeException{
    public PageControllerException(String message, Throwable e) {super(message,e);}
}
