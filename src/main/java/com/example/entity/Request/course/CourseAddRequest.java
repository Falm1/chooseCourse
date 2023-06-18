package com.example.entity.Request.course;

import com.example.entity.domain.AuthUser;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 添加课程请求类
 */
@Data
public class CourseAddRequest {

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
    private Integer status = 0;

    /**
     * 课程附带对象
     */
    private Long parentId = 0L;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;

}
