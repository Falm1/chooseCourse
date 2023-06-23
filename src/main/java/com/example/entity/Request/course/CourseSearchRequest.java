package com.example.entity.Request.course;

import com.example.entity.Request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSearchRequest extends PageRequest implements Serializable {

    /**
     * 课程Id
     */
    private String courseId;

    /**
     * 课程类别
     */
    private String category;

    /**
     * 最大人数
     */
    private Integer maxNum;
}
