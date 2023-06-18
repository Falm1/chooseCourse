package com.example.service.Impl;

import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Grade;
import com.example.mapper.CourseMapper;
import com.example.mapper.GradeMapper;
import com.example.mapper.UserMapper;
import com.example.utils.factory.grade.GradeFactory;
import com.example.utils.job.ImportData;
import com.example.utils.job.PreCache;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static com.example.constant.CourseConstant.REDIS_COURSE_KEY;
import static com.example.constant.CourseConstant.REDIS_FALM_COURSE;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    CourseMapper courseMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    ImportData importData;

    @Resource
    PreCache preCache;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Resource
    GradeMapper gradeMapper;

    @Test
    public void test(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthUser user = new AuthUser();
        user.setUsername("2020218099");
        user.setPassword(encoder.encode("falm200212"));
        user.setAvatar(" ");
        user.setRole(0);
        user.setName("yhy");
        user.setGender(1);
        user.setGrade("2020");
        user.setInstitute("aaa");
        user.setMajor("aaa");
        user.setPhone("aaa");
        user.setEmail("aaa");
        user.setStatus(1);
        user.setCreateUser("111");
        user.setCreateTime(new Date());
        user.setModifyUser("111");
        user.setModifyTime(new Date());
        user.setIsDelete(0);
        userMapper.insertUser(user);
    }

    @Test
    public void test2(){
        importData.importStudentData();
    }

    @Test
    public void ThreadTest(){
        importData.importCourse();
    }

    @Test
    public void preCacheJob(){
        preCache.preCacheCourse();
    }

    @Test
    public void test5(){
        System.out.println(courseMapper.getall());
    }


//    @Test
//    public void testRabbitMQCum(){
//        System.out.println(redisTemplate.keys("*"));
//        System.out.println(redisTemplate.opsForHash().get(REDIS_COUNT_KEY,  "443043201628"));
//        System.out.println(redisTemplate.opsForHash().get(REDIS_COURSE_KEY,  "443043201628"));
//    }

    @Test
    public void courseToMemory(){
        RLock lock = redissonClient.getLock(REDIS_FALM_COURSE);
        if(lock.tryLock()){
            try{
                Map<Object, Object> courseMap = redisTemplate.opsForHash().entries(REDIS_COURSE_KEY);
                for (Map.Entry<Object, Object> entry : courseMap.entrySet()) {
                    Long courseId = Long.valueOf(String.valueOf(entry.getKey()));
                    Set<String> userIdSet = (Set<String>) entry.getValue();
                    for (String studentId : userIdSet) {
                        if(gradeMapper.getGradeBySidAndCid(studentId, courseId)!=null) {
                            continue;
                        }
                        GradeFactory factory = new GradeFactory();
                        Grade grade = factory.getGrade(studentId, courseId);
                        gradeMapper.chooseCourse(grade);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if(lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        }
    }
}