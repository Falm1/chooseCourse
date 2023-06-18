package com.example.mapper;

import com.example.entity.domain.Grade;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

/**
* @author Falm
* @description 针对表【db_grade】的数据库操作Mapper
* @createDate 2023-06-05 18:05:58
* @Entity com.example.entity.domain.DbGrade
*/
@Mapper
public interface GradeMapper {

    @Update("UPDATE " +
            "   db_grade " +
            "SET " +
            "   isDelete = 1 " +
            "WHERE " +
            "   courseId = #{courseId}")
    int deleteGradeByCouSeId(Long courseId);

    /**
     * 选课
     * @param grade
     * @return
     */
    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("INSERT INTO " +
            "   db_grade(studentId, courseId, testGrade, workGrade, examGrade, finalGrade, createUser, createTime, modifyUser, modifyTime, isDelete) " +
            "VALUES " +
            "   (#{studentId}, #{courseId}, #{testGrade}, " +
            "    #{workGrade}, #{examGrade}, #{finalGrade}, " +
            "    #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    int chooseCourse(Grade grade);

    /**
     * 根据学号和课程号查找信息
     * @param studentId 学号
     * @param courseId 课程号
     * @return 课程信息
     */
    @Select("SELECT " +
            "   * " +
            "FROM " +
            "   db_grade " +
            "WHERE " +
            "   studentId = #{studentId} " +
            "AND " +
            "   courseId = #{courseId} " +
            "AND " +
            "   isDelete = 0")
    Grade getGradeBySidAndCid(@Param("studentId") String studentId, @Param("courseId") Long courseId);

    /**
     * 逻辑删除信息
     * @param id
     */
    @Update("UPDATE db_grade SET isDelete = 1 WHERE id = #{id}")
    void deleteGradeById(Long id);
}




