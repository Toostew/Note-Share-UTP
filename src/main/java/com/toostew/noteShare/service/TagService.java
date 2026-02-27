package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.impl.TagsDAOImpl;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.entity.jointable.File_records_tags;
import com.toostew.noteShare.exception.pojo.DAO.TagsDAOException;
import com.toostew.noteShare.exception.pojo.service.TagServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    //return all tags of a file_record
    public List<Tags> getAllTagsFromFile_records(File_records file_records){
        try{
            //get a list of every file_records_tags linked to a file_records
            List<File_records_tags> file_records_tagsList = file_records.getFile_records_tags();
            List<Tags> tagsList = new ArrayList<Tags>();

            //for each file_records_tags, get the tags
            for(File_records_tags file_records_tags : file_records_tagsList){
                tagsList.add(file_records_tags.getTags());
            }

            return tagsList;
        } catch(Exception e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tags (unknown issue)",e);
        }
    }

    //get all associated File_records that share a specified tag
    public List<File_records> getAllFile_records(Tags tags){
        try{
            List<File_records_tags> file_records_tagsList = tags.getFile_records_tags();
            List<File_records> file_recordsList = new ArrayList<File_records>();

            for(File_records_tags file_records_tags : file_records_tagsList){
                file_recordsList.add(file_records_tags.getFile_records());
            }

            return file_recordsList;
        } catch(Exception e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tags (unknown issue)",e);
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
            List<Tags> tagList = new ArrayList<>();

            for(String tag : tags){
                if(tagExists(tag)){
                    Tags tempTag = getTagByName(tag);
                    tagList.add(tempTag);
                } else {
                    //tag doesn't exist, so create it
                    Tags tempTag = new Tags();
                    tempTag.setTag_name(tag);
                    createTag(tempTag); //we upload the tag first, then recapture the tag
                    Tags tempTag2 = getTagByName(tempTag.getTag_name());
                    tagList.add(tempTag2);
                }
            }

            return tagList;
        } catch(TagServiceException e) {
            throw new  TagServiceException("Issue in Tag Service, couldn't get tags",e);
        } catch(Exception e) {
            throw new TagServiceException("Issue in Tag Service, couldn't get tags (unknown issue)",e);
        }

    }




}
