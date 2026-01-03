package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.DAOInterface;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.other.FileServiceException;
import com.toostew.noteShare.exception.pojo.other.File_recordsDAOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FileService {
    //this service is a layer for CRUD operations for File_records entities
    //DONE: naming convention: rename the files on upload to follow a convention before uploading to object storage and mysql
    //idea: uuid-datecreated

    private DAOInterface dao;



    public FileService(DAOInterface dao) {
        this.dao = dao;
    }

    //generate a UUID for storing files following naming convention:
    //UUID-DATE-CREATED
    public String createNewFileRecordStoredName(){
        UUID uuid = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        return uuid.toString()+"-"+now.toString();
    }



    //create
    public void createFile_record(File_records file_records){
        try{
            dao.createFile_records(file_records);
        } catch(File_recordsDAOException e){
            throw new FileServiceException(e.getMessage(),e);
        }

    }

    //read
    public File_records getFile_recordById(int id){
        try{
            return dao.getFile_recordById(id);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with fetching File_record by id",e);
        }

    }

    //update

    //delete
    public void deleteFile_record(int id){
        try{
            dao.deleteFile_recordById(id);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with deleting File_record by id",e);
        }
    }




}
