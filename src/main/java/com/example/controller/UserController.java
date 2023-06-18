package com.example.controller;

import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.UserVO;
import com.example.entity.Request.course.ChooseCourseRequest;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.service.UserService;
import com.example.utils.RestBean;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户中心")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/auth/valid")
    public RestBean<Boolean> validUsername(String username){
        if(StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "学号/学工号不能为空");
        }
        boolean res = userService.validUsername(username);
        return RestBean.success("验证成功", res);
    }

    @PostMapping("/auth/updatePassword")
    public RestBean<Boolean> updatePassword(UpdatePwdRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL);
        }
        Boolean res = userService.updatePassword(request);
        return RestBean.success("密码修改成功！", res);
    }

    @GetMapping("/auth/getMe")
    public RestBean<UserVO> getMe(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        UserVO user = userService.getMe(request);
        return RestBean.success("获取登录用户成功", user);
    }


    @PostMapping("/choose-course")
    public RestBean<Boolean> chooseCourse(@RequestParam("courseId") Long courseId, HttpServletRequest request){
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        ChooseCourseRequest chooseCourseRequest = new ChooseCourseRequest();
        UserVO user = userService.getMe(request);
        chooseCourseRequest.setCourseId(courseId);
        chooseCourseRequest.setUser(user);
        Boolean res = userService.chooseCourse(courseId, user);
        return RestBean.success("选课成功", res);
    }

    @PostMapping("/exit-course")
    public RestBean<Boolean> exitCourse(@RequestParam("courseId") Long courseId, HttpServletRequest request){
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        ChooseCourseRequest chooseCourseRequest = new ChooseCourseRequest();
        UserVO user = userService.getMe(request);
        chooseCourseRequest.setCourseId(courseId);
        chooseCourseRequest.setUser(user);
        Boolean res = userService.exitCourse(courseId, user);
        return RestBean.success("退课成功", res);
    }
}
