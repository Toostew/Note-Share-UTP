package com.toostew.noteShare.DAO;

import com.toostew.noteShare.entity.Course;
import com.toostew.noteShare.entity.File_records;

import java.util.List;

public interface CourseDAOInterface {

    void createCourse(Course course);

    Course getCourse(int id);

    List<Course> getAllCourses();

    void updateCourse(Course course);

    void deleteCourse(int id);

    //for bidirectional, getting File_records

    List<File_records> getFileRecordsList(int id); //returns all File_records associated with a given course id

}
