package com.example.mapper;

import com.example.entity.VO.CourseVO;
import com.example.entity.domain.Course;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Mapper
public interface CourseMapper {

    /**
     * 新建课程
     * @param course 课程
     * @return 插入条数
     */
    @Insert("INSERT INTO " +
            "   db_course " +
            "values " +
            "   (#{courseId}, #{courseName}, #{teacherId}, " +
            "    #{status}, #{parentId}, #{createUser}, " +
            "    #{createTime}, #{modifyUser}, #{modifyTime}, " +
            "    #{isDelete}, #{maxNum})")
    int addCourse(Course course);

    /**
     * 根据课程Id查找课程
     * @param courseId 课程Id
     * @return 课程
     */
    @Select("SELECT " +
            "   * " +
            "FROM " +
            "   db_course " +
            "WHERE " +
            "   courseId = #{courseId} AND isDelete = 0")
    Course getCourseByCourseId(Long courseId);

    /**
     * 修改课程信息
     * @param course 新的课程信息
     * @return 修改条数
     */
    @Update("UPDATE " +
            "   db_course " +
            "SET " +
            "   courseName = #{courseName}, teacherId = #{teacherId}, maxNum = #{maxNum} " +
            "WHERE " +
            "   courseId = #{courseId} AND isDelete = 0")
    int updateCourse(@Param("courseId") Long courseId,
                     @Param("courseName") String courseName,
                     @Param("teacherId") String teacherId,
                     @Param("maxNum") Integer maxNum);


    @Select("SELECT " +
            "   * " +
            "FROM " +
            "   courseTeacherView " +
            "WHERE " +
            "   courseId = #{courseId}")
    List<CourseVO> getCourseVOByCourseId(Long courseId);

    @Select("SELECT " +
            "   * " +
            "FROM " +
            "   courseTeacherView " +
            "WHERE " +
            "   courseName = #{searchTeat} OR name = #{searchTeat}")
    List<CourseVO> getCourseVOBySearchText(String searchTeat);

    @Select("SELECT * FROM courseTeacherView")
    List<CourseVO> getAllCourseVO();

    @Update("UPDATE db_course SET isDelete = 1 WHERE courseId = #{courseId}")
    int deleteCourse(Long courseId);
    @Select("select count(*) from courseTeacherView")
    int getall();

    @Select("SELECT COUNT(*) FROM db_course WHERE courseId = #{courseId}")
    Integer getCountByCourseId(long courseId);

    @Select("SELECT studentId FROM db_grade WHERE courseId = #{courseId}")
    Set<String> getCourseWithUser(Long courseId);
}
