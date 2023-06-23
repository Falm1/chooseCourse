package com.example.service.Impl;

import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Grade;
import com.example.entity.domain.SC;
import com.example.mapper.CourseMapper;
import com.example.mapper.ScMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.factory.grade.SCFactory;
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
import java.util.HashMap;
import java.util.Map;

import static com.example.constant.CourseConstant.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserMapper userMapper;

    @Resource
    RedissonClient redissonClient;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    ScMapper scMapper;

    @Resource
    CourseMapper courseMapper;

    @Test
    public void insertStudent(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthUser user = new AuthUser();
        user.setUsername("2020218051");
        user.setPassword(encoder.encode("falm200212"));
        user.setRole(2);
        user.setCreateUser("");
        user.setCreateTime(new Date());
        user.setModifyUser("");
        user.setModifyTime(new Date());
        user.setIsDelete(0);

        userMapper.addUser(user);
    }

    @Test
    public void courseToMemory(){
        RLock lock = redissonClient.getLock(REDIS_FALM_COURSE);
        if(lock.tryLock()){
            try{
                //存入Grade表
                Map<Object, Object> gradeMap = redisTemplate.opsForHash().entries(REDIS_COURSE_KEY);
                for (Map.Entry<Object, Object> entry : gradeMap.entrySet()) {
                    String courseId = String.valueOf(entry.getKey());
                    HashMap<String, Integer> userIdMap = (HashMap<String, Integer>) entry.getValue();
                    for (Map.Entry<String, Integer> studentEntry : userIdMap.entrySet()) {
                        String studentId = studentEntry.getKey();
                        if(scMapper.getSCByStudentIdAndCourseId(studentId, courseId) != null){
                            continue;
                        }
                        Integer isDelete = studentEntry.getValue();
                        SCFactory scFactory = new SCFactory();
                        SC selectCourse = scFactory.getSC(studentId, courseId, isDelete);
                        scMapper.addSC(selectCourse);
                    }
                }
                //更新Course表
                Map<Object, Object> courseMap = redisTemplate.opsForHash().entries(REDIS_COUNT_KEY);
                for (Map.Entry<Object, Object> courseEntry : courseMap.entrySet()) {
                    Long courseId = Long.valueOf((String) courseEntry.getKey());
                    Integer num = (Integer) courseEntry.getValue();
                    courseMapper.updateCourseNum(courseId, num);
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

    @Test
    public void test(){
        System.out.println(userMapper.getStudentByCourseId("1"));
    }

}