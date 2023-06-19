package com.example.entity.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName db_course
 */
@Data
public class Course{

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
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private Integer isDelete;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;

    /**
     * 已选人数
     */
    private Integer num;
}