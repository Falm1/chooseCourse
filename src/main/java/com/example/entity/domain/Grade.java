package com.example.entity.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName db_grade
 */
@Data
public class Grade implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 课程号
     */
    private Long courseId;

    /**
     * 实验成绩
     */
    private String testGrade;

    /**
     * 作业成绩
     */
    private String workGrade;

    /**
     * 考试成绩
     */
    private String examGrade;

    /**
     * 最终成绩
     */
    private String finalGrade;

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
}