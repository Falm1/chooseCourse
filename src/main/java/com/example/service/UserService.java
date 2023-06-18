package com.example.service;

import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.UserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

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
     *
     * @param courseId 课程Id
     * @param request Http请求
     * @return
     */
    Boolean chooseCourse(Long courseId, UserVO user);

    /**
     * 退课
     *
     * @param courseId 课程Id
     * @param user     登录用户
     * @return
     */
    Boolean exitCourse(Long courseId, UserVO user);
}
