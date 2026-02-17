package com.toostew.noteShare.test;


import com.toostew.noteShare.DAO.impl.File_recordsFileDAOImpl;
import com.toostew.noteShare.DAO.impl.File_records_tagsDAO;
import com.toostew.noteShare.DAO.impl.TagsDAOImpl;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.entity.jointable.File_records_tags;
import org.springframework.stereotype.Service;



//this is a developer only service simulator for simulating live transactions
//controller DI'd this service, so you can use the /test endpoint to simulate requests
@Service
public class TestService {

    private File_records_tagsDAO file_records_tagsDAO;
    private TagsDAOImpl tagsDAO;
    private File_recordsFileDAOImpl file_recordsFileDAO;

    public TestService(File_records_tagsDAO fileRecordsTagsDAO, TagsDAOImpl tagsDAO, File_recordsFileDAOImpl fileRecordsFileDAO) {
        this.file_records_tagsDAO = fileRecordsTagsDAO;
        this.tagsDAO = tagsDAO;
        this.file_recordsFileDAO = fileRecordsFileDAO;
    }

    public void test1(int file_records_id){

        
    }



}
