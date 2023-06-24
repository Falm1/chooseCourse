package com.example.utils.factory.course;
import java.util.Date;

import com.example.entity.domain.Course;
import com.example.utils.algorithm.SnowMaker;
import com.example.utils.factory.course.AbstractCourseFactory;

public class CourseFactory extends AbstractCourseFactory {
    @Override
    public Course getCourse(String courseId, String teacherId, Long parentId, Integer status, Integer maxNum, Integer num) {
        Course course = new Course();
        course.setTeacherId(teacherId);
        course.setStatus(status);
        course.setParentId(parentId);
        course.setCreateUser("");
        course.setCreateTime(new Date());
        course.setModifyUser("");
        course.setModifyTime(new Date());
        course.setIsDelete(0);
        course.setMaxNum(maxNum);
        course.setNum(num);
        return course;
    }
}
