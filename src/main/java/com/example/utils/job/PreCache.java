package com.example.utils.job;

import com.example.entity.VO.CourseVO;
import com.example.mapper.CourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.constant.CourseConstant.REDIS_FALM_COURSE;

/**
 * 定时任务提前预热，获取课程信息
 * 实际选课功能应该为选课前一天手动预热最好
 */
@Slf4j
@Component
public class PreCache {

    //此处缓存预热可能会有分布式问题，即多个服务器都预热，浪费性能，因此添加分布式锁

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    CourseMapper courseMapper;

    @Resource
    RedissonClient redissonClient;

    @Scheduled(cron = "0 0 10 * * *")
    public void preCacheCourse(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String redisKey = REDIS_FALM_COURSE;
        RLock lock = redissonClient.getLock(redisKey+":LOCK:KEY");
        try{
            if(lock.tryLock(0, 30000L, TimeUnit.MICROSECONDS)){
                List<CourseVO> courseList = courseMapper.getAllCourseVO();
                valueOperations.set(redisKey, courseList, 12, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            log.info(redisKey+":ERROR");
            e.printStackTrace();
        } finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
