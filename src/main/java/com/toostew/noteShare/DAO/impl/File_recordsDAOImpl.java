package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.DAOInterface;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.other.File_recordsDAOException;
import jakarta.persistence.*;
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
        try{
            entityManager.persist(file_records);
        } catch(EntityExistsException e){
            throw new File_recordsDAOException("Issue at File_recordsDAO, Entry already exists in database!",e);
        } catch(IllegalArgumentException e){
            throw new File_recordsDAOException("Issue at File_recordsDAO, received instance is not an entity!",e);
        }

    }

    @Override
    public File_records getFile_recordById(int id) {
        try{
            return entityManager.find(File_records.class, id);
        } catch (IllegalArgumentException e){
            //this might occur if the given id value is not in the database
            throw new File_recordsDAOException("Issue at File_recordsDAO, Provided arguments are problematic!",e);
        }

    }

    @Override
    public List<File_records> getFile_records(int num) {
        //if num is <= 0, retrieve all records, if above 0, retrieve first num records
        try{
            TypedQuery<File_records> query = entityManager.createQuery("from File_records f", File_records.class);
            if(num > 0){
                query.setMaxResults(num);
            }
            List<File_records> file_records_list = query.getResultList();
            return file_records_list;
        } catch(PersistenceException e){
            throw new File_recordsDAOException("Issue at File_recordsDAO, Persistence exception!",e);
        } catch(IllegalArgumentException e){
            throw new File_recordsDAOException("Issue at File_recordsDAO, JDQL query issue!",e);
        }

    }

    @Override
    @Transactional
    public void deleteFile_recordById(int id) {
        try{
            File_records temp =  entityManager.find(File_records.class, id);
            entityManager.remove(temp);
        } catch(IllegalArgumentException e){
            throw new File_recordsDAOException("Issue  at File_recordsDAO, received instance is not an entity!",e);
        }

    }
}
