package com.example.constant;

public interface CourseConstant {

    /**
     * Redis存储Course信息主键
     */
    String REDIS_FALM_COURSE = "REDIS:FALM:COURSE";

    /**
     * 保存选择课程的学生
     */
    String REDIS_COURSE_USER = "REDIS:COURSE:USER";

    /**
     * 保存学生选择的课程
     */
    String REDIS_COURSE_COUNT = "REDIS:COURSE:COUNT";

    /**
     * 获取备选课程Hash的KEY
     */
    String REDIS_COURSE_KEY = "REDIS:COURSE:KEY";

    /**
     * 获取学生选课选的课程KEY
     */
    String REDIS_STUDENT_KEY = "REDIS:STUDENT:KEY";

    /**
     * 获取备选课程数目的Hash的KEY
     */
    String REDIS_COUNT_KEY = "REDIS:COUNT:KEY";

}
