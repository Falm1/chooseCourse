package com.example.entity.VO;

import lombok.Data;

@Data
public class MyCourseVO {

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
