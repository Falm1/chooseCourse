package com.example.entity.Request.course;

import lombok.Data;

@Data
public class CourseSearchRequest {

    /**
     * 关键字查找
     */
    private String searchText;

    /**
     * 课程Id直接查找
     */
    private Long courseId;

    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 当前页最大数据
     */
    private Integer pageSize;
}
