package com.example.utils.job;

import com.example.entity.domain.AuthUser;
import com.example.mapper.CourseMapper;
import com.example.mapper.UserMapper;
import com.example.utils.factory.course.CourseFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class ImportData {

    @Resource
    UserMapper userMapper;

    @Resource
    CourseMapper courseMapper;

//    public void importStudentData(){
//        int num = 1000;
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        for(int i = 0; i<num;i++){
//            AuthUser user = new AuthUser();
//            user.setUsername(String.valueOf(2020100000+i));
//            user.setPassword(encoder.encode("falm200212"));
//            user.setRole(1);
//            user.setCreateUser("");
//            user.setCreateTime(new Date());
//            user.setModifyUser("");
//            user.setModifyTime(new Date());
//            user.setIsDelete(0);
//            userMapper.insertUser(user);
//        }
//
//    }

//    public void importCourse(){
//        int num = 10000;
//        int temp = 0;
//        for(int i = 0; i<num;i++){
//            if(i == 1000){
//                temp = 0;
//            }
//            CourseFactory factory = new CourseFactory();
//            courseMapper.addCourse(factory.getCourse("test"+i, String.valueOf(2020100000+temp++), 0L, 0, 150, 0));
//        }
//    }
}
