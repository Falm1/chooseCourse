package com.example.service;

import com.example.entity.Request.Grade.GradeAddRequest;
import com.example.entity.VO.GradeVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GradeService {
    /**
     * 添加成绩
     * @param gradeAddRequest 根据课程Id添加成绩
     * @param request http请求
     * @return 是否添加成功
     */
    Boolean addGrade(GradeAddRequest gradeAddRequest, HttpServletRequest request);

    /**
     * 根据当前登录用户获取成绩
     * @param request http请求
     * @return 成绩列表
     */
    List<GradeVO> getMyGrade(HttpServletRequest request);
}
