package com.example.service.Impl;

import com.example.entity.Request.course.CourseAddRequest;
import com.example.entity.Request.course.CourseDeleteRequest;
import com.example.entity.Request.course.CourseSearchRequest;
import com.example.entity.Request.course.CourseUpdateRequest;
import com.example.entity.VO.CourseVO;
import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Course;
import com.example.entity.enums.UserStatusEnum;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.mapper.CourseMapper;
import com.example.mapper.GradeMapper;
import com.example.service.CourseService;
import com.example.service.UserService;
import com.example.utils.factory.course.CourseFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.constant.CourseConstant.REDIS_FALM_COURSE;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    UserService userService;

    @Resource
    CourseMapper courseMapper;

    @Resource
    GradeMapper gradeMapper;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean addCourses(CourseAddRequest courseAddRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        if(user == null){
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        if(courseAddRequest == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        Integer status = courseAddRequest.getStatus();
        switch (status) {
            case 0 -> addCourse(courseAddRequest, user, status);
            case 1, 2, 3 -> addWithCourse(courseAddRequest, user);
            default -> throw new BusinessException(ErrorCode.PARAMS_ERROR, "添加类型错误");
        }

        return true;
    }

    /**
     * 添加课程
     * @param courseAddRequest
     * @param user
     */
    private void addWithCourse(CourseAddRequest courseAddRequest, UserVO user) {

    }

    /**
     * 添加属性
     * @param courseAddRequest
     * @param user
     * @param status
     */
    private void addCourse(CourseAddRequest courseAddRequest, UserVO user, Integer status) {

    }

    //缓存双删是为了保持缓存与数据库一致性
    //出现数据不一致主要原因还是因为修改数据库和修改缓存不是原子操作，会被其他线程插入
    //先删缓存，更新数据库可能会导致 进程1删除缓存，进程2进入，读取缓存为空，便读取数据库并修改缓存，之后线程一才修改了数据库，最后导致缓存中为旧数据，数据库中为新数据
    //先改数据库，再删除缓存，可能出现 缓存删除失败，导致不一致，但这种情况很少
    //但推荐使用缓存延迟双删，即修改先删一次，修改后删一次，再修改后删一次是为了删除再修改数据库这段时间内其他线程存入的脏数据
    //延时是为了保证其他线程能够执行完毕
    //此处还可以使用RabbitMQ，使用fansout，直接控制删除缓存和数据库同时进行
    @Override
    public boolean updateCourse(CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) throws InterruptedException {
        redisTemplate.delete(REDIS_FALM_COURSE);
        UserVO user = userService.getMe(request);
        userService.isAdmin(user);
        Long courseId = courseUpdateRequest.getCourseId();
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        Course course = courseMapper.getCourseByCourseId(courseId);
        //获取课程信息
        String courseName = courseUpdateRequest.getCourseName();
        if(courseName == null){
            courseName = course.getCourseName();
        }
        String teacherId = courseUpdateRequest.getTeacherId();
        if(teacherId == null){
            teacherId = course.getTeacherId();
        }
        Integer maxNum = courseUpdateRequest.getMaxNum();
        if(maxNum == null){
            maxNum = course.getMaxNum();
        }
        int res = courseMapper.updateCourse(courseId, courseName, teacherId, maxNum);
        if(res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        Thread.sleep(500);
        redisTemplate.delete(REDIS_FALM_COURSE);
        return true;
    }

    @Override
    public PageInfo<CourseVO> getCourses(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        //校验参数
        Integer pageNum = courseSearchRequest.getPageNum();
        if(pageNum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        Integer pageSize = courseSearchRequest.getPageSize();
        if(pageSize == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        Long courseId = courseSearchRequest.getCourseId();
        if(courseId != null){
            PageHelper.startPage(pageNum, pageSize);
            List<CourseVO> courseVOList = courseMapper.getCourseVOByCourseId(courseId);
            return new PageInfo<>(courseVOList, 5);
        }
        String searchText = courseSearchRequest.getSearchText();
        if(searchText != null){
            PageHelper.startPage(pageNum, pageSize);
            List<CourseVO> courseVOList = courseMapper.getCourseVOBySearchText(searchText);
            return new PageInfo<>(courseVOList, 5);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CourseVO> courseVOList = courseMapper.getAllCourseVO();
        return new PageInfo<>(courseVOList, 5);
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public boolean deleteCourse(CourseDeleteRequest courseDeleteRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        userService.isAdmin(user);
        Long courseId = courseDeleteRequest.getCourseId();
        if(courseId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        Course course = courseMapper.getCourseByCourseId(courseId);
        if(course == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        //删除课程以及学生选课的信息
        //使用事务保持数据的一致性
        int res = courseMapper.deleteCourse(courseId);
        if(res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        res = gradeMapper.deleteGradeByCouSeId(courseId);
        if(res!=1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return true;
    }
}
