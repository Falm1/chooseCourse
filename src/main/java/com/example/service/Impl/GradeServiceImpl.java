package com.example.service.Impl;
import java.util.Date;

import com.example.entity.Request.Grade.GradeAddRequest;
import com.example.entity.VO.GradeVO;
import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Grade;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.mapper.GradeMapper;
import com.example.service.GradeService;
import com.example.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Resource
    UserService userService;

    @Resource
    GradeMapper gradeMapper;

    @Override
    public Boolean addGrade(GradeAddRequest gradeAddRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        userService.isTeacher(user);
        Long couId = gradeAddRequest.getCouId();
        if(couId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择课程");
        }
        BigDecimal grade = gradeAddRequest.getGrade();
        if(grade == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "请填写分数");
        }
        Grade oldGrade = gradeMapper.getGradeByCouId(couId);
        if(oldGrade != null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该课程以有成绩");
        }
        Grade newGrade = new Grade();
        newGrade.setId(couId);
        newGrade.setGrade(grade);
        newGrade.setCreateUser("");
        newGrade.setCreateTime(new Date());
        newGrade.setModifyUser("");
        newGrade.setModifyTime(new Date());
        newGrade.setIsDelete(0);

        Integer res = gradeMapper.addGrade(newGrade);
        if(res!=1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return true;
    }

    @Override
    public List<GradeVO> getMyGrade(HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        userService.isStudent(user);
        String studentId = user.getUsername();
        if(studentId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请重新登录");
        }
        return gradeMapper.getGradeByStudentId(studentId);
    }
}
