package com.example.entity.Request.course;

import com.example.entity.VO.UserVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChooseCourseRequest implements  Serializable{

    /**
     * 课程号
     */
    Long courseId;

    /**
     * 当前登录用户
     */
    UserVO user;
}
