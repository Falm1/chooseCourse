package com.example.controller;

import com.example.entity.Request.Grade.GradeAddRequest;
import com.example.entity.VO.GradeVO;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.service.GradeService;
import com.example.utils.RestBean;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/grade")
@Api(tags = "成绩中心")
public class GradeController {

    @Resource
    GradeService gradeService;

    @PostMapping("/add-grade")
    public RestBean<Boolean> addGrade(GradeAddRequest gradeAddRequest, HttpServletRequest request){
        if(gradeAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        Boolean res = gradeService.addGrade(gradeAddRequest, request);
        return RestBean.success("添加成绩成功", res);
    }

    @PostMapping("/get-grade")
    public RestBean<List<GradeVO>> getMyGrade(HttpServletRequest request){
        List<GradeVO> gradeList = gradeService.getMyGrade(request);
        return RestBean.success("获取成绩成功", gradeList);
    }
}
