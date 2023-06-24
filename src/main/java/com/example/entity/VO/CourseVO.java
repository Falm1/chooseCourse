package com.example.entity.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseVO implements Serializable {
    /**
     * db_course表中Id
     */
    private Long couId;

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
    private String name;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类别
     */
    private String category;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 已选人数
     */
    private Integer num;

    /**
     * 类型 0-课程 1-实验 2-作业 3-期末考试
     */
    private Integer status;

    /**
     * 课程分数占比
     */
    private Double percent;
}
