package com.toostew.noteShare.DAO;

import com.toostew.noteShare.entity.Course;

import java.util.List;

public interface CourseDAOInterface {

    void createCourse(Course course);

    Course getCourse(int id);

    List<Course> getAllCourses();

    void updateCourse(Course course);

    void deleteCourse(int id);

}
