package com.example.entity.Request.course;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseDeleteRequest implements Serializable {
    /**
     * 主键Id
     */
    private Long id;
}
