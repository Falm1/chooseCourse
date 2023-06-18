package com.example.entity.Request.course;

import lombok.Data;

import java.util.Date;

@Data
public class CourseUpdateRequest {

    /**
     * 课程号
     */
    private Long courseId;

    /**
     * 名称
     */
    private String courseName;

    /**
     * 教师号
     */
    private String teacherId;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;
}
