package com.example.utils.factory.course;

import com.example.entity.domain.Course;

public abstract class AbstractCourseFactory {
    public abstract Course getCourse(String courseId, String teacherId, Long parentId, Integer status, Integer maxNum, Integer num);
}
