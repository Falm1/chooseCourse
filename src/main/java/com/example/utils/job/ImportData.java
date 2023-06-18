package com.example.utils.job;
import java.util.Date;

import com.example.entity.domain.AuthUser;
import com.example.entity.domain.Course;
import com.example.mapper.CourseMapper;
import com.example.mapper.UserMapper;
import com.example.utils.factory.course.CourseFactory;
import com.zaxxer.hikari.util.ClockSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ImportData {

    @Resource
    UserMapper userMapper;

    @Resource
    CourseMapper courseMapper;

    public void importStudentData(){
        int num = 1;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for(int i = 0; i<num;i++){
            AuthUser user = new AuthUser();
            user.setUsername(String.valueOf(2020328051+i));
            user.setPassword(encoder.encode("falm200212"));
            user.setAvatar("");
            user.setRole(2);
            user.setName("test"+i);
            user.setGender(0);
            user.setGrade("2020");
            user.setInstitute("test");
            user.setMajor("test");
            user.setPhone("test");
            user.setEmail("test");
            user.setCreateUser("");
            user.setCreateTime(new Date());
            user.setModifyUser("");
            user.setModifyTime(new Date());
            user.setIsDelete(0);
            userMapper.insertUser(user);
        }

    }

    public void importCourse(){
        int num = 1;
        for(int i = 0; i<num;i++){
            CourseFactory factory = new CourseFactory();
//            courseMapper.addCourse(factory.getCourse("test"+i, String.valueOf(1020328051+i), 150, 0L));
        }
    }
}
