package com.example.entity.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyCourseVO implements Serializable {

    /**
     * 学生Id
     */
    private String studentId;

    /**
     * 课程Id
     */
    private String courseId;

    /**
     * 教师号
     */
    private String teacherId;


    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 教师名称
     */
    private String studentName;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类别
     */
    private String category;

    /**
     * 已选人数
     */
    private Integer num;
}
