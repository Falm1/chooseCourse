package com.example.utils.job;

import com.example.entity.domain.SC;
import com.example.mapper.CourseMapper;
import com.example.mapper.ScMapper;
import com.example.utils.factory.grade.SCFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

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
    ScMapper scMapper;

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
}
