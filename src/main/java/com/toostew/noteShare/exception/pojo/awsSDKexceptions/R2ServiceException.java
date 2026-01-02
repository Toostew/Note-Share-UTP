package com.toostew.noteShare.exception.pojo.awsSDKexceptions;


//error occurs at the R2Service layer
public class R2ServiceException extends RuntimeException{
    public R2ServiceException(String message, Throwable e){
        super(message, e);
    }
}
