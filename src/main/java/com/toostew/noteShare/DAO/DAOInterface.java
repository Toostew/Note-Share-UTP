package com.toostew.noteShare.DAO;

import com.toostew.noteShare.entity.File_records;

import java.util.List;

public interface DAOInterface {

    void createFile_records(File_records file_records);

    File_records getFile_recordById(int id);

    List<File_records> getFile_records();

    void deleteFile_recordById(int id);


}
