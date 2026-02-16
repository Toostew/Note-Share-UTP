package com.toostew.noteShare.DAO.impl;


import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.jointable.File_records_tags;
import com.toostew.noteShare.entity.jointable.File_records_tagsEmbeddedKey;
import com.toostew.noteShare.exception.pojo.DAO.File_records_tagsDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class File_records_tagsDAO {

    private EntityManager em;

    public File_records_tagsDAO(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createFile_records_tags(File_records_tags file_records_tags){
        try {
            em.persist(file_records_tags);
        } catch (EntityExistsException e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, Entity already exists",e);
        } catch (Exception e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, unknown issue", e);
        }
    }
    //embeddedKey is alias for Embedded ID which is JPA solution for Composite Keys
    public File_records_tags getFile_records_tags(File_records_tagsEmbeddedKey embeddedKey){
        try {
            return em.find(File_records_tags.class, embeddedKey);
        } catch (EntityNotFoundException e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, Entity doesnt exist",e);
        } catch (Exception e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, unknown issue", e);
        }
    }

    @Transactional
    public void updateFile_records_tags(File_records_tags file_records_tags){
        try {
            File_records_tags temp = em.find(File_records_tags.class, file_records_tags.getId());
            temp.setFile_records(file_records_tags.getFile_records());
            temp.setTags(file_records_tags.getTags());
            em.merge(temp);
        } catch (EntityNotFoundException e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, Entity doesnt exist",e);
        } catch (Exception e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, unknown issue", e);
        }
    }

    @Transactional
    public void deleteFile_records_tags(File_records_tagsEmbeddedKey embeddedKey){
        try {
            File_records_tags temp = em.find(File_records_tags.class, embeddedKey);
            em.remove(temp);
        } catch (EntityNotFoundException e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, Entity doesnt exist",e);
        } catch (Exception e) {
            throw new File_records_tagsDAOException("issue in File_records_tagsDAO, unknown issue", e);
        }
    }
}
