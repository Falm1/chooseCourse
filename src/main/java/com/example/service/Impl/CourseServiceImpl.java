package com.example.service.Impl;
import java.util.Date;
import com.example.entity.VO.*;
import com.example.entity.domain.With;
import com.google.common.collect.Lists;

import com.example.entity.Request.course.CourseAddRequest;
import com.example.entity.Request.course.CourseDeleteRequest;
import com.example.entity.Request.course.CourseSearchRequest;
import com.example.entity.Request.course.CourseUpdateRequest;
import com.example.entity.domain.Course;
import com.example.entity.domain.Student;
import com.example.entity.enums.CourseStatusEnum;
import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import com.example.mapper.CourseMapper;
import com.example.mapper.ScMapper;
import com.example.mapper.UserMapper;
import com.example.service.CourseService;
import com.example.service.UserService;
import com.example.utils.factory.course.CourseFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.constant.CourseConstant.REDIS_COUNT_KEY;
import static com.example.constant.CourseConstant.REDIS_FALM_COURSE;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    UserService userService;

    @Resource
    CourseMapper courseMapper;

    @Resource
    ScMapper scMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加课程
     * @param courseAddRequest
     * @param  request
     */
    @Transactional(rollbackFor = BusinessException.class)
    public boolean addCourse(CourseAddRequest courseAddRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        Integer status = courseAddRequest.getStatus();
        if(status == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        CourseStatusEnum courseStatusEnum = CourseStatusEnum.getCourseStatusEnum(status);
        if(courseStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "系统错误");
        }
        String courseName = courseAddRequest.getCourseName();
        if(courseName == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        String teacherId = courseAddRequest.getTeacherId();
        if(teacherId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        Long parentId = courseAddRequest.getParentId();
        if(parentId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        Integer maxNum = courseAddRequest.getMaxNum();
        if(maxNum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数错误");
        }
        Integer num = courseAddRequest.getNum();
        if(num == null){
            num = 0;
        }
        BigDecimal percent = courseAddRequest.getPercent();
        if(percent == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "参数错误");
        }
        //添加详细信息表
        With with = new With();
        with.setCourseName(courseName);
        with.setPercent(percent);
        with.setCreateUser("");
        with.setCreateTime(new Date());
        with.setModifyUser("");
        with.setModifyTime(new Date());
        with.setIsDelete(0);
        Integer res = null;
        if(courseStatusEnum.equals(CourseStatusEnum.Work)){
            res = courseMapper.addWork(with);
        } else if(courseStatusEnum.equals(CourseStatusEnum.Test)){
            res = courseMapper.addTest(with);
        } else if(courseStatusEnum.equals(CourseStatusEnum.Exam)){
            res = courseMapper.addExam(with);
        }
        if(res == null || res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        Long couId = with.getId();
        Course course = new Course();
        course.setCourseId(couId);
        course.setTeacherId(teacherId);
        course.setStatus(status);
        course.setParentId(parentId);
        course.setCreateUser("");
        course.setCreateTime(new Date());
        course.setModifyUser("");
        course.setModifyTime(new Date());
        course.setIsDelete(0);
        course.setMaxNum(maxNum);
        course.setNum(num);
        res = courseMapper.addCourse(course);
        if(res == null || res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return true;
    }


    @Override
    public boolean updateCourse(CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) throws InterruptedException {
        redisTemplate.delete(REDIS_FALM_COURSE);
        UserVO user = userService.getMe(request);
        if(!userService.isAdmin(user)){
            throw new BusinessException(ErrorCode.NO_AUTH,"用户无权限");
        }
        Long id = courseUpdateRequest.getId();
        if(id == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        Course course = courseMapper.getCourseById(id);
        if(course == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "找不到该课程");
        }
        //获取课程信息
        Integer maxNum = courseUpdateRequest.getMaxNum();
        if(maxNum == null){
            maxNum = course.getMaxNum();
        }
        String teacherId = courseUpdateRequest.getTeacherId();
        if(teacherId == null){
            teacherId = course.getTeacherId();
        }
        int res = courseMapper.updateCourse(id, maxNum, teacherId);
        if(res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        Thread.sleep(500);
        redisTemplate.delete(REDIS_FALM_COURSE);
        return true;
    }

    //此处取出选课人数可能不是最新的，因此需要将redis中最新人数更新到pageInfo中
    //todo 暂时只实现根据课程号搜索，后续使用elastic搜索引擎实现关键字搜索
    @Override
    public PageInfo<CourseVO> getCourses(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        List<CourseVO> courseVOList;
        String studentId = user.getUsername();
        if(studentId == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        //校验参数
        Integer pageNum = courseSearchRequest.getPageNum();
        if(pageNum == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        Integer pageSize = courseSearchRequest.getPageSize();
        if(pageSize == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "参数为空");
        }
        String courseId = courseSearchRequest.getCourseId();
        if(courseId != null){
            PageHelper.startPage(pageNum, pageSize);
            CourseVO courseVO = courseMapper.getCourseVOByCourseId(courseId);
            courseVOList = new ArrayList<>();
            courseVOList.add(courseVO);
            PageInfo<CourseVO> coursePage = new PageInfo<>(courseVOList, 5);
            return getCourseWithNum(coursePage);
        }
        PageHelper.startPage(pageNum, pageSize);
        courseVOList = courseMapper.getAllCourseVO();
        PageInfo<CourseVO> coursePage = new PageInfo<>(courseVOList, 5);
        return getCourseWithNum(coursePage);
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public boolean deleteCourse(CourseDeleteRequest courseDeleteRequest, HttpServletRequest request) {
        UserVO user = userService.getMe(request);
        Long id = courseDeleteRequest.getId();
        if(id == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        Course course = courseMapper.getCourseById(id);
        if(course == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL, "找不到该课程");
        }
        //鉴权
        Integer status = course.getStatus();
        CourseStatusEnum courseStatusEnum = CourseStatusEnum.getCourseStatusEnum(status);
        if(CourseStatusEnum.Course.equals(courseStatusEnum)){
            userService.isAdmin(user);
        }else{
            userService.isTeacher(user);
        }
        //删除课程以及学生选课的信息
        //使用事务保持数据的一致性
        int res = courseMapper.deleteCourse(id);
        if(res != 1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        res = scMapper.deleteSCByCourseId(course.getCourseId());
        if(res<0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return true;
    }

    @Override
    public CourseDetails getCourseDetails(String courseId) {
        CourseDetails courseDetails = new CourseDetails();
        CourseVO courseVO = courseMapper.getCourseVOByCourseId(courseId);
        BeanUtils.copyProperties(courseVO, courseDetails);
        //获取该课程附属信息
        List<CourseVO> courseList = courseMapper.getWithByCourseId(courseId);
        //获取选该课程学生
        List<UserVO> userVOList = userMapper.getStudentByCourseId(courseId);
        courseDetails.setCourseList(courseList);
        courseDetails.setStudentList(userVOList);
        return courseDetails;
    }

    @Override
    public List<CourseCategory> getCourseCategory(String courseId) {
        List<CourseCategory> courseCategorieList = new ArrayList<>();
        List<CourseCate> courseList = courseMapper.getWithByCourseIdAndStatus(courseId, 1);
        CourseCategory test = new CourseCategory();
        test.setValue(1L);
        test.setLabel("实验");
        test.setChildren(courseList);
        courseCategorieList.add(test);
        courseList = courseMapper.getWithByCourseIdAndStatus(courseId, 2);
        CourseCategory work = new CourseCategory();
        work.setValue(2L);
        work.setLabel("作业");
        work.setChildren(courseList);
        courseCategorieList.add(work);
        courseList = courseMapper.getWithByCourseIdAndStatus(courseId, 3);
        CourseCategory exam = new CourseCategory();
        exam.setValue(3L);
        exam.setLabel("考试");
        exam.setChildren(courseList);
        courseCategorieList.add(exam);
        return courseCategorieList;
    }

    /**
     * 从缓存中更新已选人数
     * @param pageInfo 课程信息
     * @return 更新人数后的课程信息
     */
    private PageInfo<CourseVO> getCourseWithNum(PageInfo<CourseVO> pageInfo){
        List<CourseVO> courseList = pageInfo.getList();
        for (CourseVO courseVO : courseList) {
            String courseId = courseVO.getCourseId();
            if(courseId == null){
                continue;
            }
            Integer num = (Integer) redisTemplate.opsForHash().get(REDIS_COUNT_KEY, courseId);
            if(num == null){
                continue;
            }
            courseVO.setNum(num);
        }
        pageInfo.setList(courseList);
        return pageInfo;
    }
}
