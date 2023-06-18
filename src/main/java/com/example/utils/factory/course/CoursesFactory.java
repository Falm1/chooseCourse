package com.example.utils.factory.course;

import com.example.entity.domain.Course;

/**
 * 使用工厂模式创建课程以及课程附带的对象
 * @param <T> 类型
 */
public  interface CoursesFactory <T extends Course>{
    T getCourse(String courseName, String teacherId, Integer maxNum, Long parentId, Integer status);
}
