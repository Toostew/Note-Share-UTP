package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.DAOInterface;
import com.toostew.noteShare.entity.File_records;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    //this service is a layer for CRUD operations for File_records entities
    //TODO: naming convention: rename the files on upload to follow a convention before uploading to object storage and mysql
    //idea: id-originalname-datecreated

    private DAOInterface dao;



    public FileService(DAOInterface dao) {
        this.dao = dao;
    }

    //create
    public void createFile_record(File_records file_records){
        dao.createFile_records(file_records);
    }

    //read
    public File_records getFile_recordById(int id){
        return dao.getFile_recordById(id);
    }

    //update

    //delete
    public void deleteFile_record(int id){
        dao.deleteFile_recordById(id);
    }




}
