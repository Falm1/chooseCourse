package com.example.mapper;

import com.example.entity.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author Falm
* @description 针对表【db_student_info】的数据库操作Mapper
* @createDate 2023-06-22 10:35:10
* @Entity com.example.entity.domain.DbStudentInfo
*/
@Mapper
public interface StudentMapper {

    /**
     * 根据学生Id获取学生信息
     * @param studentId 学生Id
     * @return 学生信息
     */
    @Select("SELECT * FROM db_student_info WHERE studentId = #{studentId} and isDelete = 0")
    Student getStudentByStudentId(String studentId);
}




