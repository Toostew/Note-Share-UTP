package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.CourseDAOInterface;
import com.toostew.noteShare.entity.Course;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.DAO.CourseDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAOInterface {

    private EntityManager em;

    public CourseDAOImpl(EntityManager em) {
        this.em = em;
    }


    //at the current moment there is no need to update or delete courses via java, this can be done via DDA
    @Transactional
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

    @Transactional
    @Override
    public void updateCourse(Course course) {
        //at the moment can only modify course name and category
        try {
            Course temp = em.find(Course.class, course.getId());
            temp.setName(course.getName());
            temp.setCategory(course.getCategory());
            em.merge(temp);
        } catch(EntityNotFoundException e){
            throw new CourseDAOException("Issue in CourseDAO, Course not found!", e);
        }  catch(IllegalArgumentException e){
            throw new CourseDAOException("Issue in CourseDAO, Illegal argument!", e);
        }
        System.out.println("updated course of id: " + course.getId());

    }

    @Transactional
    @Override
    public void deleteCourse(int id) {
        try {
            Course temp = em.find(Course.class, id);
            em.remove(temp);
        } catch(EntityNotFoundException e){
            throw new CourseDAOException("Issue in CourseDAO, Course not found!", e);
        } catch(IllegalArgumentException e){
            throw new CourseDAOException("Issue in CourseDAO, Illegal argument!", e);
        }
        System.out.println("deleted course of id: " + id);

    }


    //File_record handling
    @Override
    public List<File_records> getFileRecordsList(int courseId) {
        try{
            Course temp = em.find(Course.class, courseId);
            return temp.getFile_recordsList();
        } catch(EntityNotFoundException e){
            throw new CourseDAOException("Issue in CourseDAO, Course not found!", e);
        }

    }
}
