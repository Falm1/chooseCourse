package com.example.controller;

import com.example.entity.Request.PageRequest;
import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.MyCourseVO;
import com.example.entity.VO.UserVO;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.service.UserService;
import com.example.utils.RestBean;
import com.github.pagehelper.PageInfo;
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

    /**
     * 校验用户名是否正确
     * @param username 用户名
     * @return 是否正确
     */
    @PostMapping("/auth/valid")
    public RestBean<Boolean> validUsername(String username){
        if(StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "学号/学工号不能为空");
        }
        boolean res = userService.validUsername(username);
        return RestBean.success("验证成功", res);
    }

    /**
     * 重置密码
     * @param request http请求
     * @return 是否修改成功
     */
    @PostMapping("/auth/updatePassword")
    public RestBean<Boolean> updatePassword(UpdatePwdRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL);
        }
        Boolean res = userService.updatePassword(request);
        return RestBean.success("密码修改成功！", res);
    }

    /**
     * 获取当前登录用户
     * @param request http请求
     * @return 当前登录用户信息
     */
    @GetMapping("/auth/getMe")
    public RestBean<UserVO> getMe(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        UserVO user = userService.getMe(request);
        return RestBean.success("获取登录用户成功", user);
    }


    /**
     * 选课
     * @param courseId 课程编号
     * @param request http请求
     * @return 是否选课成功
     */
    @PostMapping("/choose-course")
    public RestBean<Boolean> chooseCourse(@RequestParam("courseId") String courseId, HttpServletRequest request){
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        UserVO user = userService.getMe(request);
        Boolean res = userService.chooseCourse(courseId, user);
        return RestBean.success("选课成功", res);
    }

    /**
     * 退课
     * @param courseId 课程编号
     * @param request http请求
     * @return 是否退课成功
     */
    @PostMapping("/exit-course")
    public RestBean<Boolean> exitCourse(@RequestParam("courseId") String courseId, HttpServletRequest request){
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        UserVO user = userService.getMe(request);
        Boolean res = userService.exitCourse(courseId, user);
        return RestBean.success("退课成功", res);
    }

    /**
     * 获取当前登录用户的已选课程
     * @param pageRequest 分页请求
     * @param request http请求
     * @return 课程信息
     */
    @PostMapping("/get-myCourse")
    public RestBean<PageInfo<MyCourseVO>> getMyCourse(PageRequest pageRequest, HttpServletRequest request){
        if(pageRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        PageInfo<MyCourseVO> page = userService.getMyCourses(pageRequest, request);
        return RestBean.success("获取课程成功", page);
    }

}
