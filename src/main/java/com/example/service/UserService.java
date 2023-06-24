package com.example.service;

import com.example.entity.Request.PageRequest;
import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.MyCourseVO;
import com.example.entity.VO.UserVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {
    /**
     * 校验用户名
     *
     * @param username 用户名
     * @return 返回是否合法
     */
    boolean validUsername(String username);

    /**
     * 修改密码
     *
     * @param request Http请求
     * @return 是否修改成功
     */
    Boolean updatePassword(UpdatePwdRequest request);

    /**
     * 获取当前登录用户
     * @param request Http请求
     * @return 登录用户
     */
    UserVO getMe(HttpServletRequest request);

    /**
     * 获取用户是否是管理员
     * @param user 当前用户
     * @return 是否是管理员
     */
    boolean isAdmin(UserVO user);


    /**
     * 获取用户是否是老师
     * @param user 当前用户
     * @return 是否是老师
     */
    boolean isTeacher(UserVO user);

    /**
     * 获取用户是否是学生
     * @param user 当前用户
     * @return 是否是老师
     */
    boolean isStudent(UserVO user);

    /**
     *
     * @param courseId 课程Id
     * @param user Http请求
     * @return
     */
    Boolean chooseCourse(String courseId, UserVO user);

    /**
     * 退课
     *
     * @param courseId 课程Id
     * @param user     登录用户
     * @return
     */
    Boolean exitCourse(String courseId, UserVO user);

    /**
     * 获取当前登录用户选课列表
     * @param request http请求
     * @return 用户选课列表
     */
    PageInfo<MyCourseVO> getMyCourses(PageRequest pageRequest, HttpServletRequest request);
}
