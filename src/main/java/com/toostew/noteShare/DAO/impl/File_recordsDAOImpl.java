package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.DAOInterface;
import com.toostew.noteShare.entity.File_records;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class File_recordsDAOImpl implements DAOInterface {

    private EntityManager entityManager;


    public File_recordsDAOImpl() {}

    @Autowired
    public File_recordsDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void createFile_records(File_records file_records) {
        entityManager.persist(file_records);
    }

    @Override
    public File_records getFile_recordById(int id) {
        return entityManager.find(File_records.class, id);
    }

    @Override
    public List<File_records> getFile_records(int num) {
        //if num is <= 0, retrieve all records, if above 0, retrieve first num records
        TypedQuery<File_records> query = entityManager.createQuery("from File_records f", File_records.class);
        if(num > 0){
            query.setMaxResults(num);
        }
        List<File_records> file_records_list = query.getResultList();
        return file_records_list;
    }

    @Override
    @Transactional
    public void deleteFile_recordById(int id) {
        File_records temp =  entityManager.find(File_records.class, id);
        entityManager.remove(temp);
    }
}
