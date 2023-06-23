package com.example.controller;

import com.example.entity.Request.course.CourseAddRequest;
import com.example.entity.Request.course.CourseDeleteRequest;
import com.example.entity.Request.course.CourseSearchRequest;
import com.example.entity.Request.course.CourseUpdateRequest;
import com.example.entity.VO.CourseDetails;
import com.example.entity.VO.CourseVO;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.service.CourseService;
import com.example.utils.RestBean;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "课程中心")
@Controller
@RequestMapping("/course")
@RestController
public class CourseController {

    @Resource
    CourseService courseService;

    /**
     * 新建课程
     * @param courseAddRequest 添加课程信息封装类
     * @param request 获取请求
     * @return 是否创建成功
     */
    @PostMapping("/add-course")
    public RestBean<Boolean> addCourse(CourseAddRequest courseAddRequest, HttpServletRequest request){
        if(courseAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        boolean res = courseService.addCourse(courseAddRequest, request);
        return RestBean.success("新建课程成功", res);
    }

    /**
     * 修改课程信息
     * @param courseUpdateRequest 新的课程信息
     * @param request 请求
     * @return 是否修改成功
     */
    @PostMapping("/update-course")
    public RestBean<Boolean> updateCourse(CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) throws InterruptedException {
        if(courseUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        boolean res = courseService.updateCourse(courseUpdateRequest, request);
        return RestBean.success("修改课程成功", res);
    }

    /**
     * 搜索课程，可以根据课程名称与授课老师来查找
     * @return 课程列表
     */
    @PostMapping("/search-course")
    public RestBean<PageInfo<CourseVO>> searchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request){
        if(courseSearchRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        PageInfo<CourseVO> page = courseService.getCourses(courseSearchRequest, request);
        return RestBean.success("获取课程成功", page);
    }

    /**
     * 删除课程信息
     * @param courseDeleteRequest 课程删除信息
     * @param request 请求
     * @return 是否删除成功
     */
    @PostMapping("/delete-course")
    public RestBean<Boolean> deleteCourse(CourseDeleteRequest courseDeleteRequest, HttpServletRequest request){
        if(courseDeleteRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        boolean res = courseService.deleteCourse(courseDeleteRequest, request);
        return RestBean.success("删除课程成功", res);
    }

    @PostMapping("/get-course-details")
    public RestBean<CourseDetails> doGetCourseDetails(@RequestParam("courseId") String courseId){
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        CourseDetails courseDetails = courseService.getCourseDetails(courseId);
        return RestBean.success("获取课程信息成功", courseDetails);
    }
}
