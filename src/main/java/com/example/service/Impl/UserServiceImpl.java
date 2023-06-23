package com.example.service.Impl;

import com.example.entity.Request.PageRequest;
import com.example.entity.Request.user.UpdatePwdRequest;
import com.example.entity.VO.CourseVO;
import com.example.entity.VO.MyCourseVO;
import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import com.example.entity.domain.SC;
import com.example.entity.enums.UserRoleEnum;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.mapper.CourseMapper;
import com.example.mapper.ScMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    ScMapper scMapper;

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
        UserRoleEnum userStatusEnum = UserRoleEnum.getUserRoleEnumByRole(role);
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的学号/教职工号");
        }
        AuthUser user = userMapper.getUserByUsername(username);
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的学号/教职工号");
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
        int res = userMapper.updatePassword(username, password);
        if(res!=1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "重置密码失败");
        }
        return true;
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
        UserRoleEnum userStatusEnum = UserRoleEnum.getUserRoleEnumByRole(userRole);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        if(!userStatusEnum.equals(UserRoleEnum.ADMIN)){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        }
        return true;
    }

    public boolean isTeacher(UserVO user){
        Integer userRole = user.getRole();
        if(userRole == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        UserRoleEnum userStatusEnum = UserRoleEnum.getUserRoleEnumByRole(userRole);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        if(!userStatusEnum.equals(UserRoleEnum.TEACHER)){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        }
        return true;
    }

    public boolean isStudent(UserVO user){
        Integer userRole = user.getRole();
        if(userRole == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        UserRoleEnum userStatusEnum = UserRoleEnum.getUserRoleEnumByRole(userRole);
        if(userStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "用户异常");
        }
        if(!userStatusEnum.equals(UserRoleEnum.STUDENT)){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        }
        return true;
    }



    //使用两种场景，1. RabbitMQ 2.redis加锁   两者测试并发环境
    //对于经常需要修改的数据，将其存放到redis中，在redis中修改，之后再将其持久化到数据库
    //redis中存放 课程被哪些学生选，还有课程被选的数目
    //1.redis+锁
    @Override
    public Boolean chooseCourse(String courseId, UserVO user) {
        //只有学生能选课
        isStudent(user);
        ReentrantLock lock = new ReentrantLock(true);                           //创建公平锁，保证插入数据按创建顺序
        if(lock.tryLock()){
            try{
                String userId = user.getUsername();
                //学生添加到课程
                HashMap<String, Integer> courseMap = (HashMap<String, Integer>) redisTemplate.opsForHash().get(REDIS_COURSE_KEY, courseId);
                if(courseMap == null){
                    List<String> courseList = courseMapper.getStudentIdByCourseId(courseId);
                    courseMap = new HashMap<>();
                    for (String studentId : courseList) {
                        courseMap.put(studentId, 0);
                    }
                }
                Integer isDelete = courseMap.get(userId);
                if(courseMap.containsKey(userId) && isDelete == 0){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复选择课程");
                }
                courseMap.put(userId, 0);
                redisTemplate.opsForHash().put(REDIS_COURSE_KEY, courseId, courseMap);     //之前数据会被覆盖
                redisTemplate.expire(REDIS_COURSE_KEY, 1, TimeUnit.DAYS);
                //将课程添加到学生
                Set<String> studentSet = (Set<String>) redisTemplate.opsForHash().get(REDIS_STUDENT_KEY, userId);
                if(studentSet == null){
                    studentSet = userMapper.getCourseIdByStudentId(userId);
                }
                if(!studentSet.add(courseId)){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复选择课程");
                }
                redisTemplate.opsForHash().put(REDIS_STUDENT_KEY, userId, studentSet);     //之前数据会被覆盖
                redisTemplate.expire(REDIS_STUDENT_KEY, 1, TimeUnit.DAYS);
                //课程被选数量+1
                Integer count = (Integer) redisTemplate.opsForHash().get(REDIS_COUNT_KEY, courseId);
                if(count == null){
                    count = courseMap.size()-1;
                    redisTemplate.opsForHash().put(REDIS_COUNT_KEY, courseId, count);
                }
                if(count >= 150){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "课程已满");
                }
                redisTemplate.opsForHash().increment(REDIS_COUNT_KEY, courseId, 1);      //数量+1,不能使用redis的increment，数据库中的数据会被序列化
                redisTemplate.expire(REDIS_COUNT_KEY, 1, TimeUnit.DAYS);
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public Boolean exitCourse(String courseId, UserVO user) {
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        //只有学生能选课，进行校验
        isStudent(user);
        String userId = user.getUsername();
        //首先判断用户有没有选择这节课
        //需要从两个地方查询，数据库和redis
        SC selectCourse  = scMapper.getSCByStudentIdAndCourseId(userId, courseId);
        HashMap<String, Integer> courseMap = (HashMap<String, Integer>) redisTemplate.opsForHash().get(REDIS_COURSE_KEY, courseId);
        if(selectCourse == null){
            //数据库中没有数据，则去判断redis中有没有数据
            if(courseMap == null){
                throw new BusinessException(ErrorCode.NO_AUTH, "用户没有选择该课程");
            }
            if(!courseMap.containsKey(userId)){
                throw new BusinessException(ErrorCode.NO_AUTH, "用户没有选择该课程");
            }
        } else {
            scMapper.deleteSCById(selectCourse.getId());
        }
        //更新课程包含学生的数据
        if (courseMap != null) {
            courseMap.put(String.valueOf(userId), 1);
            redisTemplate.opsForHash().put(REDIS_COURSE_KEY, courseId, courseMap);
        }
        //更新学生包含课程的数据
        Set<String> studentSet = (Set<String>) redisTemplate.opsForHash().get(REDIS_STUDENT_KEY, userId);
        if (studentSet != null) {
            studentSet.remove(courseId);
        }
        if (studentSet != null) {
            redisTemplate.opsForHash().put(REDIS_STUDENT_KEY, userId, studentSet);
        }
        redisTemplate.opsForHash().increment(REDIS_COUNT_KEY, courseId, -1);
        return true;
    }

    @Override
    public PageInfo<MyCourseVO> getMyCourses(PageRequest pageRequest, HttpServletRequest request) {
        UserVO user = this.getMe(request);
        String username = user.getUsername();
        if(username == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        Integer pageNum = pageRequest.getPageNum();
        if(pageNum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        Integer pageSize = pageRequest.getPageSize();
        if(pageSize == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        //获取redis中某位学生选的课程号
        UserRoleEnum userRoleEnum = UserRoleEnum.getUserRoleEnumByRole(user.getRole());
        if(UserRoleEnum.STUDENT.equals(userRoleEnum)){
            List<MyCourseVO> courseList = new ArrayList<>();
            HashSet<String> courseSet = (HashSet<String>) redisTemplate.opsForHash().get(REDIS_STUDENT_KEY, username);
            if(courseSet != null){
                for (String courseId : courseSet) {
                    Integer num = (Integer) redisTemplate.opsForHash().get(REDIS_COUNT_KEY, courseId);
                    if(num == null){
                        num = courseMapper.getNumByCourseId(courseId);
                    }
                    CourseVO courseVO = courseMapper.getMyCourseVOByCourseId(courseId);
                    MyCourseVO myCourseVO = new MyCourseVO();
                    BeanUtils.copyProperties(courseVO, myCourseVO);
                    myCourseVO.setStudentId(username);
                    myCourseVO.setNum(num);
                    courseList.add(myCourseVO);
                }
                PageHelper.startPage(pageNum, pageSize);
                return new PageInfo<>(courseList, 5);
            }
            PageHelper.startPage(pageNum, pageSize);
            courseList = courseMapper.getMyCourseByUsername(username);
            return new PageInfo<>(courseList, 5);
        } else if(UserRoleEnum.TEACHER.equals(userRoleEnum)){
            String teacherId = user.getUsername();
            PageHelper.startPage(pageNum, pageSize);
            List<MyCourseVO> myCourseVOList = courseMapper.getMyCourseByTeacherId(teacherId);
            return new PageInfo<>(myCourseVOList, 5);
        }
        return null;
    }
}
