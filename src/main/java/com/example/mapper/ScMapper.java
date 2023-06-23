package com.example.mapper;

import com.example.entity.domain.SC;
import org.apache.ibatis.annotations.*;

/**
* @author Falm
* @description 针对表【db_SC】的数据库操作Mapper
* @createDate 2023-06-22 10:20:25
* @Entity com.example.entity.domain.DbSc
*/
@Mapper
public interface ScMapper {

    /**
     * 根据课程Id和学生Id获取选课信息
     * @param studentId 学生Id
     * @param courseId 课程Id
     * @return 选课信息
     */
    @Select("SELECT * FROM db_SC WHERE studentId = #{studentId} AND courseId = #{courseId} and isDelete = 0")
    SC getSCByStudentIdAndCourseId(@Param("studentId") String studentId, @Param("courseId") String courseId);

    /**
     * 删除某选课信息
     * @param id 选课Id
     */
    @Update("update db_SC set isDelete = 1 where id = #{id}")
    void deleteSCById(Long id);

    /**
     * 添加选课信息
     * @param selectCourse 选课信息
     */
    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("INSERT INTO db_SC(studentId, courseId, createUser, createTime, modifyUser, modifyTime, isDelete) " +
            "VALUES(#{studentId}, #{courseId}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    void addSC(SC selectCourse);

    /**
     * 根据课程ID删除选课信息
     * @param courseId 课程Id
     * @return 是否删除成功
     */
    @Update("UPDATE db_SC SET isDelete = 1 WHERE courseId = #{courseId}")
    int deleteSCByCourseId(String courseId);
}




