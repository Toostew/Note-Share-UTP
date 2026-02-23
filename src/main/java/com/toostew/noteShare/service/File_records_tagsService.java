package com.toostew.noteShare.service;


import com.toostew.noteShare.DAO.impl.File_records_tagsDAO;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.entity.jointable.File_records_tags;
import com.toostew.noteShare.entity.jointable.File_records_tagsEmbeddedKey;
import com.toostew.noteShare.exception.pojo.DAO.File_records_tagsDAOException;
import com.toostew.noteShare.exception.pojo.service.File_records_tagsServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class File_records_tagsService {

    private File_records_tagsDAO file_records_tagsDAO;

    public File_records_tagsService(File_records_tagsDAO file_records_tagsDAO){
        this.file_records_tagsDAO=file_records_tagsDAO;
    }


    //create File_record_tags link accepting existing file_records and tags
    public void createFile_records_tagsWithTagList(File_records file_records, List<Tags> tags){

    }

    public File_records_tags getFile_records_tagsWithFile_recordsAndTags(File_records file_records, Tags tags){
        try {
            File_records_tagsEmbeddedKey file_records_tagsEmbeddedKey = new File_records_tagsEmbeddedKey(file_records.getId(),  tags.getId());
            return file_records_tagsDAO.getFile_records_tags(file_records_tagsEmbeddedKey);
        } catch (File_records_tagsDAOException e){
            throw new File_records_tagsServiceException("Issue in File_records_tags Service, could not get File record tags", e);
        }

    }

    public File_records_tags getFile_records_tagsWithID(int file_records_id, int tagsID){
        try {
            File_records_tagsEmbeddedKey file_records_tagsEmbeddedKey = new File_records_tagsEmbeddedKey(file_records_id,tagsID);
            return file_records_tagsDAO.getFile_records_tags(file_records_tagsEmbeddedKey);
        } catch (File_records_tagsDAOException e){
            throw new File_records_tagsServiceException("Issue in File_records_tags Service, could not get File record tags", e);
        }
    }

    public void updateFile_records_tags(File_records_tags file_records_tags){
        try {
            file_records_tagsDAO.updateFile_records_tags(file_records_tags);
        } catch(File_records_tagsDAOException e){
            throw new File_records_tagsServiceException("Issue in File_records_tags Service, could not update File record tags", e);
        }
    }

    public void deleteFile_records_tags(int File_records_id, int tagsID){
        try {
            File_records_tagsEmbeddedKey file_records_tagsEmbeddedKey = new File_records_tagsEmbeddedKey(File_records_id,tagsID);
            file_records_tagsDAO.deleteFile_records_tags(file_records_tagsEmbeddedKey);
        } catch(File_records_tagsDAOException e){
            throw new File_records_tagsServiceException("Issue in File_records_tags Service, could not delete File record tags", e);
        }
    }



}
