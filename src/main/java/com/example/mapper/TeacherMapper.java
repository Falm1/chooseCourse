package com.example.mapper;

import com.example.entity.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author Falm
* @description 针对表【db_teacher_info】的数据库操作Mapper
* @createDate 2023-06-22 10:41:18
* @Entity com.example.entity.domain.DbTeacherInfo
*/
@Mapper
public interface TeacherMapper {

    /**
     * 根据教师ID查询教师信息
     * @param teacherId 教师编号
     * @return 教师信息
     */
    @Select("select * from db_teacher_info where teacherId = #{teacherId} and isDelete = 0")
    Teacher getTeacherByTeacherId(String teacherId);
}




