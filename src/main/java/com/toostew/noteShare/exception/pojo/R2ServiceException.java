package com.toostew.noteShare.exception.pojo;


//this error occurs if the R2 Server themselves have issues, in that case there is nothing we can do but abort
public class R2ServiceException extends RuntimeException{
    public R2ServiceException(String message){
        super(message);
    }
}
