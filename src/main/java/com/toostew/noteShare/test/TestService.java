package com.toostew.noteShare.test;


import com.toostew.noteShare.DAO.impl.File_recordsFileDAOImpl;
import com.toostew.noteShare.DAO.impl.TagsDAOImpl;
import org.springframework.stereotype.Service;



//this is a developer only service simulator for simulating live transactions
//controller DI'd this service, so you can use the /test endpoint to simulate requests
@Service
public class TestService {


    private TagsDAOImpl tagsDAO;
    private File_recordsFileDAOImpl file_recordsFileDAO;

    public TestService( TagsDAOImpl tagsDAO, File_recordsFileDAOImpl fileRecordsFileDAO) {
        this.tagsDAO = tagsDAO;
        this.file_recordsFileDAO = fileRecordsFileDAO;
    }

    public void test1(int file_records_id){


    }



}
