package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.CourseDAOInterface;
import com.toostew.noteShare.entity.Course;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.other.CourseDAOException;
import com.toostew.noteShare.exception.pojo.other.CourseServiceException;
import com.toostew.noteShare.exception.pojo.other.OwnerDAOException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private CourseDAOInterface courseDAO;

    public  CourseService(CourseDAOInterface courseDAO) {
        this.courseDAO = courseDAO;
    }

    public void createCourse(Course course){
        try{
            courseDAO.createCourse(course);
        } catch(CourseDAOException e){
            throw new CourseServiceException("Issue in Course Service, could not create course",e);
        }
    }

    public Course getCourse(int id){
        try{
            return courseDAO.getCourse(id);
        } catch(CourseDAOException e){
            throw new CourseServiceException("Issue in Course Service, could not get course",e);
        } catch(IllegalArgumentException e){
            throw new CourseServiceException("Issue in Course Service, could not get course",e);
        }
    }

    public List<Course> getAllCourses(){
        try{
            return courseDAO.getAllCourses();
        } catch(CourseDAOException e){
            throw new CourseServiceException("Issue in Course Service, could not get all courses",e);
        }
    }

    public List<File_records> getFile_recordsList(int courseId){
        try{
            return courseDAO.getFileRecordsList(courseId);
        } catch(CourseDAOException e){
            throw new CourseServiceException("Issue in Course Service, could not get file records",e);
        }
    }

    //delete and update courses are not implemented as there is currently no need for java to be able to delete or
    //update courses directly, this can be done using MYSQL workbench


}
