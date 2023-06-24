package com.example.entity.Request.course;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseAddRequest implements Serializable {
    /**
     * 名称
     */
    private String courseName;

    /**
     * 教师号
     */
    private String teacherId;

    /**
     * 类型 0-课程 1-实验 2-作业 3-期末考试
     */
    private Integer status;

    /**
     * 课程附带对象
     */
    private Long parentId;

    /**
     * 课程最大人数
     */
    private Integer maxNum;

    /**
     * 课程已选人数
     */
    private Integer num;

    /**
     * 占百分比
     */
    private BigDecimal percent;

}
