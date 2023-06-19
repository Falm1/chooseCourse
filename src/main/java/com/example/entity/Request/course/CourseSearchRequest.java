package com.example.entity.Request.course;

import com.example.entity.Request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSearchRequest extends PageRequest {

    /**
     * 关键字查找
     */
    private String searchText;

    /**
     * 课程Id直接查找
     */
    private Long courseId;
}
