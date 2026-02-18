package com.toostew.noteShare.service;


import com.toostew.noteShare.DAO.impl.ThumbnailDAO;
import com.toostew.noteShare.entity.Thumbnail;
import com.toostew.noteShare.exception.pojo.DAO.ThumbnailDAOException;
import com.toostew.noteShare.exception.pojo.service.ThumbnailServiceException;
import org.springframework.stereotype.Service;


//service to generate Thumbnails and talk to ThumbnailDAO
@Service
public class ThumbnailService {

    private ThumbnailDAO thumbnailDAO;

    public ThumbnailService(ThumbnailDAO thumbnailDAO) {
        this.thumbnailDAO = thumbnailDAO;
    }

    public void createThumbnail(Thumbnail thumbnail){
        try {
            thumbnailDAO.createThumbnail(thumbnail);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("Issue in ThumbnailService, could not create thumbnail",e);
        }
    }

    public Thumbnail getThumbnail(int id){
        try {
            return thumbnailDAO.getThumbnail(id);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("Issue in ThumbnailService, could not get thumbnail",e);
        }
    }

    public void updateThumbnail(Thumbnail thumbnail){
        try {
            thumbnailDAO.updateThumbnail(thumbnail);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("Issue in ThumbnailService, could not update thumbnail",e);
        }
    }

    public void deleteThumbnail(int id){
        try {
            thumbnailDAO.deleteThumbnail(id);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("Issue in ThumbnailService, could not delete thumbnail",e);
        }
    }

}
