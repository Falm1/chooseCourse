package com.example.service;

import com.example.entity.Request.course.CourseAddRequest;
import com.example.entity.Request.course.CourseDeleteRequest;
import com.example.entity.Request.course.CourseSearchRequest;
import com.example.entity.Request.course.CourseUpdateRequest;
import com.example.entity.VO.CourseCategory;
import com.example.entity.VO.CourseDetails;
import com.example.entity.VO.CourseVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CourseService {

    /**
     * 新建课程
     * @param courseAddRequest 课程信息
     * @param request 获取登录对象鉴权
     * @return 是否创建成功
     */
    boolean addCourse(CourseAddRequest courseAddRequest, HttpServletRequest request);

    /**
     * 修改课程
     * @param courseUpdateRequest 新的课程信息
     * @param request 获取登录对象鉴权
     * @return 是否修改成功
     */
    boolean updateCourse(CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) throws InterruptedException;

    /**
     * 查找队伍列表
     * @param courseSearchRequest 查找课程信息
     * @param request 查询当前用户
     * @return 符合要求课程列表
     */
    PageInfo<CourseVO> getCourses(CourseSearchRequest courseSearchRequest, HttpServletRequest request);

    /**
     * 删除课程
     * @param courseDeleteRequest 删除课程
     * @param request 获取当前用户
     * @return 是否删除成功
     */
    boolean deleteCourse(CourseDeleteRequest courseDeleteRequest, HttpServletRequest request);

    /**
     * 获取某课程的详细信息
     * @param courseId 课程Id
     * @return 课程信息
     */
    CourseDetails getCourseDetails(String courseId);

    /**
     * 获取课程信息的分类表，便于前端展示
     * @param courseId 课程号
     * @return 课程分类列表
     */
    List<CourseCategory> getCourseCategory(String courseId);
}
