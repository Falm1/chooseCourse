package com.example.entity.Request.course;

import lombok.Data;

@Data
public class CourseDeleteRequest {
    /**
     * 课程Id直接查找
     */
    private Long courseId;
}
