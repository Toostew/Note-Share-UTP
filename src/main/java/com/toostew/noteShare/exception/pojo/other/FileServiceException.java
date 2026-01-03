package com.toostew.noteShare.exception.pojo.other;

//issue occurred at FileService layer
public class FileServiceException extends RuntimeException{
    public FileServiceException(String message, Throwable e){
        super(message,e);
    }
}
