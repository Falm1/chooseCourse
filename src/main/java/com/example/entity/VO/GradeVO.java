package com.example.entity.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeVO {

    /**
     * 课程号
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师名称
     */
    private String name;

    /**
     * 学生编号
     */
    private String studentId;

    /**
     * 实验成绩
     */
    private BigDecimal testGrade;

    /**
     * 作业成绩
     */
    private BigDecimal workGrade;

    /**
     * 考试成绩
     */
    private BigDecimal examGrade;

    /**
     * 最终成绩
     */
    private BigDecimal finalGrade;
}
