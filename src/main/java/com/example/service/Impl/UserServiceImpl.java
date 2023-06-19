package com.example.service.Impl;

import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Course;
import com.example.entity.domain.Grade;
import com.example.entity.enums.UserStatusEnum;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.mapper.CourseMapper;
import com.example.mapper.GradeMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.factory.grade.GradeFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.constant.CourseConstant.*;
import static com.example.constant.UserConstant.*;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    CourseMapper courseMapper;

    @Resource
    GradeMapper gradeMapper;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取用户信息提供给Security校验
     *
     * @param username 用户名
     * @return 用户登录信息
     * @throws UsernameNotFoundException 用户未找到
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userMapper.getUserByUsername(username);
        if(user == null) throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        Integer role = user.getRole();
        UserStatusEnum userStatusEnum = UserStatusEnum.getUserStatusEnumByRole(role);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        String roleText = userStatusEnum.getText();
        if(roleText == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL);
        }
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleText)
                .build();
    }

    @Override
    public boolean validUsername(String username) {
        if(!username.matches(USERNAME_REGEX)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的学号");
        }
        AuthUser user = userMapper.getUserByUsername(username);
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的学号");
        }
        return true;
    }

    @Override
    public Boolean updatePassword(UpdatePwdRequest request) {
        String username = request.getUsername();
        if(StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "学号/学工号不能为空");
        }
        this.validUsername(username);
        String password = request.getPassword();
        if(StringUtils.isBlank(password)){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "密码不能为空");
        }
        if(!password.matches(PASSWORD_REGEX)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码格式不正确");
        }
        String checkPassword = request.getCheckPassword();
        if(StringUtils.isBlank(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "密码不能为空");
        }
        if(!checkPassword.matches(PASSWORD_REGEX)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码格式不正确");
        }
        if(!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        password = encoder.encode(password);
        return userMapper.updatePassword(username, password);
    }

    @Override
    public UserVO getMe(HttpServletRequest request) {
        UserVO user = (UserVO)request.getSession().getAttribute(USER_LOGIN_STATE);
        if(user == null){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        return user;
    }

    @Override
    public boolean isAdmin(UserVO user) {
        Integer userRole = user.getRole();
        if(userRole == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        UserStatusEnum userStatusEnum = UserStatusEnum.getUserStatusEnumByRole(userRole);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        if(!userStatusEnum.equals(UserStatusEnum.ADMIN)){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        }
        return true;
    }

    public boolean isTeacher(UserVO user){
        Integer userRole = user.getRole();
        if(userRole == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        UserStatusEnum userStatusEnum = UserStatusEnum.getUserStatusEnumByRole(userRole);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        if(!userStatusEnum.equals(UserStatusEnum.TEACHER)){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        }
        return true;
    }

    //使用两种场景，1. RabbitMQ 2.redis加锁   两者测试并发环境
    //对于经常需要修改的数据，将其存放到redis中，在redis中修改，之后再将其持久化到数据库
    //redis中存放 课程被哪些学生选，还有课程被选的数目
    //1.redis+锁
    @Override
    public Boolean chooseCourse(Long courseId, UserVO user) {
        ReentrantLock lock = new ReentrantLock(true);                           //创建公平锁，保证插入数据按创建顺序
        if(lock.tryLock()){
            try{
                String userId = user.getUsername();
                String redisCourseId = courseId.toString();
                //学生添加到课程
                HashMap<String, Integer> courseMap = (HashMap<String, Integer>) redisTemplate.opsForHash().get(REDIS_COURSE_KEY, courseId.toString());
                if(courseMap == null){
                    List<String> courseList = courseMapper.getCourseWithUser(courseId);
                    courseMap = new HashMap<>();
                    for (String studentId : courseList) {
                        courseMap.put(studentId, 0);
                    }
                }
                if(courseMap.containsKey(userId)){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复选择课程");
                }
                courseMap.put(userId, 0);
                redisTemplate.opsForHash().put(REDIS_COURSE_KEY, redisCourseId, courseMap);     //之前数据会被覆盖
                redisTemplate.expire(REDIS_COURSE_KEY, 1, TimeUnit.DAYS);
                //将课程添加到学生
                Set<String> studentSet = (Set<String>) redisTemplate.opsForHash().get(REDIS_STUDENT_KEY, userId);
                if(studentSet == null){
                    studentSet = userMapper.getCourseIdByStudentId(userId);
                }
                if(!studentSet.add(String.valueOf(courseId))){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复选择课程");
                }
                redisTemplate.opsForHash().put(REDIS_STUDENT_KEY, userId, studentSet);     //之前数据会被覆盖
                redisTemplate.expire(REDIS_STUDENT_KEY, 1, TimeUnit.DAYS);
                //课程被选数量+1
                Integer count = (Integer) redisTemplate.opsForHash().get(REDIS_COUNT_KEY, redisCourseId);
                if(count == null){
                    count = courseMap.size()-1;
                    redisTemplate.opsForHash().put(REDIS_COUNT_KEY, redisCourseId, count);
                }
                if(count >= 150){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "课程已满");
                }
                redisTemplate.opsForHash().increment(REDIS_COUNT_KEY, redisCourseId, 1);      //数量+1,不能使用redis的increment，数据库中的数据会被序列化
                redisTemplate.expire(REDIS_COUNT_KEY, 1, TimeUnit.DAYS);
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public Boolean exitCourse(Long courseId, UserVO user) {
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        String userId = user.getUsername();
        //首先判断用户有没有选择这节课
        //需要从两个地方查询，数据库和redis
        Grade grade = gradeMapper.getGradeBySidAndCid(userId, courseId);
        HashMap<String, Integer> courseMap = (HashMap<String, Integer>) redisTemplate.opsForHash().get(REDIS_COURSE_KEY, courseId.toString());
        if(grade == null){
            //数据库中没有数据，则去判断redis中有没有数据
            if(courseMap == null){
                throw new BusinessException(ErrorCode.NO_AUTH, "用户没有选择该课程");
            }
            if(!courseMap.containsKey(userId)){
                throw new BusinessException(ErrorCode.NO_AUTH, "用户没有选择该课程");
            }
        } else {
            gradeMapper.deleteGradeById(grade.getId());
        }
        //更新课程包含学生的数据
        if (courseMap != null) {
            courseMap.put(String.valueOf(userId), 1);
            redisTemplate.opsForHash().put(REDIS_COURSE_KEY, courseId.toString(), courseMap);
        }
        //更新学生包含课程的数据
        Set<String> studentSet = (Set<String>) redisTemplate.opsForHash().get(REDIS_STUDENT_KEY, userId);
        if (studentSet != null) {
            studentSet.remove(String.valueOf(courseId));
        }
        if (studentSet != null) {
            redisTemplate.opsForHash().put(REDIS_STUDENT_KEY, userId, studentSet);
        }
        redisTemplate.opsForHash().increment(REDIS_COUNT_KEY, courseId.toString(), -1);
        return true;
    }
}
