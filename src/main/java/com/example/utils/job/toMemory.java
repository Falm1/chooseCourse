package com.example.utils.job;

import com.example.entity.domain.Course;
import com.example.entity.domain.Grade;
import com.example.mapper.CourseMapper;
import com.example.mapper.GradeMapper;
import com.example.utils.factory.grade.GradeFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.constant.CourseConstant.*;

/**
 * 将redis中数据持久化到数据库
 */
@Component
public class toMemory {

    @Resource
    RedissonClient redissonClient;

    @Resource
    CourseMapper courseMapper;

    @Resource
    GradeMapper gradeMapper;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0/1 * * ? ")                 //每一小时执行一次
    public void courseToMemory(){
        RLock lock = redissonClient.getLock(REDIS_FALM_COURSE);
        if(lock.tryLock()){
            try{
                //存入Grade表
                Map<Object, Object> gradeMap = redisTemplate.opsForHash().entries(REDIS_COURSE_KEY);
                for (Map.Entry<Object, Object> entry : gradeMap.entrySet()) {
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
                //更新Course表
                Map<Object, Object> courseMap = redisTemplate.opsForHash().entries(REDIS_COUNT_KEY);
                for (Map.Entry<Object, Object> courseEntry : courseMap.entrySet()) {
                    Long courseId = (Long) courseEntry.getKey();
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
}
