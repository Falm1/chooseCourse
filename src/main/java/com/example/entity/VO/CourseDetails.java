package com.example.entity.VO;

import com.example.entity.domain.Course;
import com.example.entity.domain.Student;
import lombok.Data;

import java.util.List;

@Data
public class CourseDetails {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 课程Id
     */
    private String courseId;

    /**
     * 教师号
     */
    private String teacherId;


    /**
     * 教师名称
     */
    private String name;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类别
     */
    private String category;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 已选人数
     */
    private Integer num;

    /**
     * 该课程附属信息
     */
    List<CourseVO> courseList;

    /**
     * 选择该课的学生,保存学号和姓名
     */
    List<UserVO> studentList;
}
