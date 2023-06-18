package com.example.entity.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseVO implements Serializable {

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
    private String name;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;
}
