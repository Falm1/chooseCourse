package com.example.utils.factory.course;
import java.util.Date;

import com.example.entity.domain.Course;
import com.example.utils.algorithm.SnowMaker;

public class CourseFactory implements CoursesFactory<Course> {
    @Override
    public Course getCourse(String courseName, String teacherId, Integer maxNum, Long parentId, Integer status) {
        Course course = new Course();
        SnowMaker maker = new SnowMaker(0);
        Long courseId = maker.nextId();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setTeacherId(teacherId);
        course.setParentId(0L);
        course.setCreateUser("");
        course.setCreateTime(new Date());
        course.setModifyUser("");
        course.setModifyTime(new Date());
        course.setIsDelete(0);
        course.setMaxNum(maxNum);
        return course;
    }

}
