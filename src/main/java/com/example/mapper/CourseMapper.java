package com.example.mapper;

import com.example.entity.VO.CourseCate;
import com.example.entity.VO.CourseVO;
import com.example.entity.VO.MyCourseVO;
import com.example.entity.domain.Course;
import com.example.entity.domain.With;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Falm
* @description 针对表【db_course】的数据库操作Mapper
* @createDate 2023-06-22 10:21:15
* @Entity com.example.entity.domain.DbCourse
*/
@Mapper
public interface CourseMapper {

    /**
     * 获取选择某课程的学生Id
     * @param courseId 课程Id
     * @return 学生Id列表
     */
    @Select("select studentId from db_SC where courseId = #{courseId}")
    List<String> getStudentIdByCourseId(String courseId);

    /**
     * 更新选课人数
     * @param courseId 课程号
     * @param num 已选人数
     */
    @Update("update db_course set num = #{num} where courseId = #{courseId}")
    void updateCourseNum(@Param("courseId") Long courseId, @Param("num") Integer num);

    /**
     * 根据课程号获取课程和老师的详细信息
     * @param username 学生Id
     * @param courseId 课程Id
     * @return 课程和老师的详细信息
     */
    @Select("select * from courseWithTeacher where courseId = #{courseId}")
    CourseVO getMyCourseVOByCourseId(String courseId);

    /**
     * 根据学生Id获取他选的课程的详细信息
     * @param studentId 学生Id
     * @return
     */
    @Select("SELECT * FROM studentWithCourse WHERE studentId = #{studentId}")
    List<MyCourseVO> getMyCourseByUsername(String studentId);


    /**
     * 添加课程信息
     * @param course 课程信息
     */
    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("insert into db_course(courseId, teacherId, status, parentId, createUser, createTime, modifyUser, modifyTime, isDelete, maxNum, num) " +
            "values (#{courseId}, #{teacherId}, #{status}, #{parentId}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete}, #{maxNum}, #{num})")
    Integer addCourse(Course course);

    /**
     * 根据Id查找课程
     * @param id id
     * @return 课程信息
     */
    @Select("select * from db_course where id = #{id}")
    Course getCourseById(Long id);

    /**
     *
     * @param id 课程老师Id
     * @param maxNum 最大人数
     * @param teacherId 教师Id
     * @return 是否修改成功
     */
    @Update("update db_course set maxNum = #{maxNum}, teacherId = #{teacherId} WHERE id = #{id}")
    int updateCourse(@Param("id") Long id, @Param("maxNum") Integer maxNum, @Param("teacherId") String teacherId);

    /**
     * 从视图中获取课程信息
     * @param courseId 课程编号
     * @return 课程信息
     */
    @Select("select * from courseWithTeacher where courseId = #{courseId}")
    CourseVO getCourseVOByCourseId(String courseId);

    /**
     * 从视图中获取全部的课程信息
     * @return 课程信息
     */
    @Select("SELECT * FROM courseWithTeacher")
    List<CourseVO> getAllCourseVO();

    /**
     * 删除某门课程
     * @param id 课程主键Id
     * @return 是否删除成功
     */
    @Update("update db_course set isDelete = 1 where id = #{id}")
    int deleteCourse(Long id);

    /**
     * 根据课程号获取课程的已选人数
     * @param courseId 课程Id
     */
    @Select("SELECT num FROM db_course WHERE courseId = #{courseId} AND status = 0")
    Integer getNumByCourseId(String courseId);

    /**
     * 根据教师Id获取他选的课程的详细信息
     * @param teacherId 学生Id
     * @return
     */
    @Select("SELECT * FROM studentWithCourse WHERE teacherId = #{teacherId}")
    List<MyCourseVO> getMyCourseByTeacherId(String teacherId);

    /**
     * 获取课程的附属信息
     * @param courseId 课程号
     * @return 课程附属信息
     */
    @Select("select * from anotherWithTeacher where parentId=#{courseId} order by status")
    List<CourseVO> getWithByCourseId(String courseId);

    /**
     * 根据课程号和课程类别获取某种课程信息
     * @param courseId 课程号
     * @param status 类别
     * @return
     */
    @Results({
            @Result(column = "couId", property = "value"),
            @Result(column = "courseName", property = "label"),
    })
    @Select("select couId, courseName from anotherWithTeacher where parentId=#{courseId} and status = #{status}")
    List<CourseCate> getWithByCourseIdAndStatus(@Param("courseId") String courseId, @Param("status") int status);

    /**
     * 添加作业
     * @param courseName 作业名称
     * @param percent 成绩占比
     * @return
     */
    @Options(useGeneratedKeys = true, keyColumn = "workId", keyProperty = "id")
    @Insert("INSERT INTO db_work_info(workName, percent, createUser, createTime, modifyUser, modifyTime, isDelete) " +
            "values (#{courseName}, #{percent}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    Integer addWork(With with);

    @Options(useGeneratedKeys = true, keyColumn = "testId", keyProperty = "id")
    @Insert("INSERT INTO db_test_info(testName, percent, createUser, createTime, modifyUser, modifyTime, isDelete) " +
            "values (#{courseName}, #{percent}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    Integer addTest(With with);

    @Options(useGeneratedKeys = true, keyColumn = "examId", keyProperty = "id")
    @Insert("INSERT INTO db_exam_info(examName, percent, createUser, createTime, modifyUser, modifyTime, isDelete) " +
            "values (#{courseName}, #{percent}, #{createUser}, #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    Integer addExam(With with);
}




