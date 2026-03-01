package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.impl.TagsDAOImpl;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.exception.pojo.DAO.TagsDAOException;
import com.toostew.noteShare.exception.pojo.service.TagServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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



    public boolean tagExists(String name){
        return  tagsDAOImpl.tagExists(name);
    }

    //this method accepts a String which contains a comma delimited list of tag names
    //it will then upload the tags into the database
    public List<Tags> createTagListFromString(String tagsInAString) {
        try {
            String[] tags = tagsInAString.split(","); // String array of tag names

            //for each tag name, search to see if it already exists
            //if yes, return that tag. If not, create a new tag
            Set<Tags> tagSet = new HashSet<Tags>();

            List<Tags> tagList = new ArrayList<>();

            for(String tag : tags){
                tag = tag.toLowerCase().trim();
                if(tagExists(tag)){
                    Tags tempTag = getTagByName(tag);
                    tagSet.add(tempTag);
                } else {
                    //tag doesn't exist, so create it
                    Tags tempTag = new Tags();
                    tempTag.setTag_name(tag);
                    createTag(tempTag); //we upload the tag first, then recapture the tag
                    Tags tempTag2 = getTagByName(tempTag.getTag_name());
                    tagSet.add(tempTag2);
                }
            }

            //next loop transfer them into a list, done filtering
            //TODO: there's def a better way to do this without having to use 2 different data structures
            for(Tags tag : tagSet){
                tagList.add(tag);
            }

            return tagList;
        } catch(TagServiceException e) {
            throw new  TagServiceException("Issue in Tag Service, couldn't get tags",e);
        } catch(Exception e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tags (unknown issue)",e);
        }

    }




}
