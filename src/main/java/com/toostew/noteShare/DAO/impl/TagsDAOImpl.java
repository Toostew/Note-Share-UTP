package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.entity.Tags;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.TagsDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagsDAOImpl {

    private EntityManager em;

    public TagsDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createTag(Tags tags){
        try{
            em.persist(tags);
        } catch(EntityExistsException e){
            throw new TagsDAOException("Issue in TagsDAO, That tag already exists!",e);
        } catch(Exception e){
            throw new  TagsDAOException("Issue in TagsDAO, Unknown issue",e);
        }
    }

    public Tags getTag(int id){
        try{
            return em.find(Tags.class, id);
        } catch(EntityNotFoundException e){
            throw new TagsDAOException("Issue in TagsDAO, That tag does not exist!",e);
        }  catch(Exception e){
            throw new  TagsDAOException("Issue in TagsDAO, Unknown issue",e);
        }
    }
    //getTagById is identical to getTag and is here
    public Tags getTagByName(String name){
        try{
            return em.createQuery("SELECT u FROM Tags u WHERE u.tag_name = :name", Tags.class)
                    .setParameter("name", name)
                    .getSingleResult();

        } catch(EntityNotFoundException e){
            throw new TagsDAOException("Issue in TagsDAO, That tag does not exist!",e);
        }  catch(Exception e){
            throw new  TagsDAOException("Issue in TagsDAO, Unknown issue",e);
        }
    }


    @Transactional
    public void updateTag(Tags tags){
        try{
            Tags temp = em.find(Tags.class, tags.getId());
            temp.setTag_name(tags.getTag_name());
            em.merge(temp);
        } catch(EntityNotFoundException e){
            throw new TagsDAOException("Issue in TagsDAO, That tag does not exist!",e);
        }  catch(Exception e){
            throw new  TagsDAOException("Issue in TagsDAO, Unknown issue",e);
        }
    }

    @Transactional
    public void deleteTag(int id){
        try{
            Tags temp = em.find(Tags.class, id);
            em.remove(temp);
        } catch(EntityNotFoundException e){
            throw new TagsDAOException("Issue in TagsDAO, That tag does not exist!",e);
        }   catch(Exception e){
            throw new  TagsDAOException("Issue in TagsDAO, Unknown issue",e);
        }
    }


}
