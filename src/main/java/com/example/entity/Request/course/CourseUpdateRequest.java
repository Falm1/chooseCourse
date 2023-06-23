package com.example.entity.Request.course;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CourseUpdateRequest  implements Serializable {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 主键Id
     */
    private String teacherId;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;
}
