package com.toostew.noteShare.DAO.impl;


import com.toostew.noteShare.entity.Thumbnail;
import com.toostew.noteShare.exception.pojo.DAO.ThumbnailDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ThumbnailDAO {

    private EntityManager em;

    public ThumbnailDAO(EntityManager em) {
        this.em = em;
    }

    public void createThumbnail(Thumbnail thumbnail){
        try{
            em.persist(thumbnail);
        } catch (EntityExistsException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Entity already exists", e);
        } catch (IllegalArgumentException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Invalid arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, unknown issue", e);
        }

    }

    public Thumbnail getThumbnail(int id){
        try{
            return em.find(Thumbnail.class, id);
        } catch (EntityNotFoundException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Entity does not exist", e);
        } catch (IllegalArgumentException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Invalid arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, unknown issue", e);
        }
    }

    public void updateThumbnail(Thumbnail thumbnail){
        try{
            Thumbnail temp =  em.find(Thumbnail.class, thumbnail.getId());
            temp.setStored_name(thumbnail.getStored_name());
            temp.setFile_records(thumbnail.getFile_records());
            em.merge(temp);
        } catch (EntityNotFoundException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Entity does not exist", e);
        } catch (IllegalArgumentException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Invalid arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, unknown issue", e);
        }
    }

    public void deleteThumbnail(int id){
        try{
            Thumbnail temp =  em.find(Thumbnail.class, id);
            em.remove(temp);
        } catch (EntityNotFoundException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Entity does not exist", e);
        } catch (IllegalArgumentException e){
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, Invalid arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("Issue in ThumbnailDAO, unknown issue", e);
        }
    }

}
