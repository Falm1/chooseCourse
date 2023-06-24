package com.example.mapper;

import com.example.entity.VO.GradeVO;
import com.example.entity.domain.Grade;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Falm
* @description 针对表【db_grade】的数据库操作Mapper
* @createDate 2023-06-22 13:12:35
* @Entity com.example.entity.domain.DbGrade
*/
@Mapper
public interface GradeMapper {

    /**
     * 插入成绩
     * @param grade 成绩
     * @return 影响行数
     */
    @Insert("insert into db_grade values (#{id}, #{grade}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    Integer addGrade(Grade grade);

    /**
     * 更具Id获取成绩
     * @param couId Id
     * @return
     */
    @Select("select * from db_grade where id = #{couId}")
    Grade getGradeByCouId(Long couId);

    /**
     * 根据学生学号查找成绩
     * @param studentId 学号
     * @return 成绩列表
     */
    @Select("select * from gradeWithCourse where studentId = #{studentId}")
    List<GradeVO> getGradeByStudentId(String studentId);
}




