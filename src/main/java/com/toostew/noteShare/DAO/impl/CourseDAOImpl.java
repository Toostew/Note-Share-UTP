package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.CourseDAOInterface;
import com.toostew.noteShare.entity.Course;
import com.toostew.noteShare.exception.pojo.other.CourseDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAOInterface {

    private EntityManager em;

    public CourseDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void createCourse(Course course) {
        try{
            em.persist(course);
        }catch(EntityExistsException e){
            throw new CourseDAOException("Issue in CourseDAO, Entity already exists!", e);
        } catch(IllegalArgumentException e){
            throw new CourseDAOException("Issue in CourseDAO, Illegal argument!", e);
        }
    }

    @Override
    public Course getCourse(int id) {
        try{
            return em.find(Course.class, id);
        } catch(EntityNotFoundException e){
            throw new CourseDAOException("Issue in CourseDAO, Course not found!", e);
        } catch(IllegalArgumentException e){
            throw new CourseDAOException("Issue in CourseDAO, Illegal argument!", e);
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try{
            TypedQuery<Course> query = em.createQuery("select c from Course c", Course.class);
            return query.getResultList();
        } catch(IllegalArgumentException e){
            throw new CourseDAOException("Issue in CourseDAO, Illegal argument!", e);
        } catch(Exception e){
            //this is added as placeholder to catch every other exception
            //TODO: look up all the exceptions that can occur for em.createQuery
            throw new CourseDAOException("Issue in CourseDAO, Unexpected error!", e);
        }
    }

    @Override
    public void updateCourse(Course course) {
        //not implemented, currently there is no need to be able to delete or update courses,
        //can do so directly via workbench
    }

    @Override
    public void deleteCourse(int id) {
        //not implemented, currently there is no need to be able to delete or update courses from java
        //can do so via workbench instead
    }
}
