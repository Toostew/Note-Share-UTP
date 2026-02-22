package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.impl.TagsDAOImpl;
import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.exception.pojo.DAO.TagsDAOException;
import com.toostew.noteShare.exception.pojo.service.TagServiceException;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private TagsDAOImpl tagsDAOImpl;

    public TagService(TagsDAOImpl tagsDAOImpl) {
        this.tagsDAOImpl = tagsDAOImpl;
    }

    public void createTag(Tags tags){
        try{
            tagsDAOImpl.createTag(tags);
        } catch(TagsDAOException e) {
            throw new TagServiceException("Issue in Tag Service, couldnt create tag",e);
        }

    }

    public Tags getTag(int id){
        try{
            return tagsDAOImpl.getTag(id);
        } catch(TagsDAOException e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tag",e);
        }
    }

    public Tags getTagByName(String name){
        try{
            return tagsDAOImpl.getTagByName(name);
        } catch(TagsDAOException e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tag by name",e);
        }
    }

    public void updateTag(Tags tags){
        try{
            tagsDAOImpl.updateTag(tags);
        } catch(TagsDAOException e) {
            throw new TagServiceException("Issue in Tag Service, couldn't update tag",e);
        }
    }

    public void deleteTag(int id){
        try{
            tagsDAOImpl.deleteTag(id);
        }  catch(TagsDAOException e) {
            throw new TagServiceException("Issue in Tag Service, couldn't delete tag",e);
        }
    }



}
