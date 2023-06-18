package com.example.entity.view;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 课程与教师的联系的视图
 */
@Data
@Alias("courseTeacherView")
public class CourseTeacherView {
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
    private String teacherName;

    /**
     * 课程可选最大人数
     */
    private Integer maxNum;

    /**
     * 课程已选人数
     */
    private Integer num;
}
