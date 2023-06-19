package com.example.mapper;

import com.example.entity.VO.CourseVO;
import com.example.entity.VO.MyCourseVO;
import com.example.entity.domain.Course;
import com.github.pagehelper.Page;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
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

    @Update("UPDATE " +
            "   db_course " +
            "SET " +
            "   num = #{num} " +
            "WHERE " +
            "   courseId = #{courseId} AND isDelete = 0")
    int updateCourseNum(@Param("courseId") Long courseId,
                        @Param("num") Integer num);

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
            "   courseName = #{searchTeat} OR name = #{searchText}")
    List<CourseVO> getCourseVOBySearchText(String searchText);

    @Select("SELECT courseId, courseName, name, maxNum, num FROM courseTeacherView")
    List<CourseVO> getAllCourseVO();

    @Update("UPDATE db_course SET isDelete = 1 WHERE courseId = #{courseId}")
    int deleteCourse(Long courseId);

    @Select("SELECT COUNT(*) FROM db_course WHERE courseId = #{courseId}")
    Integer getCountByCourseId(long courseId);

    @Select("SELECT studentId FROM db_grade WHERE courseId = #{courseId}")
    List<String> getCourseWithUser(Long courseId);

    @Select("SELECT COUNT(*) FROM db_grade WHERE courseId = #{courseId}")
    Integer getCourseNumByCourseId(Long courseId);

    /**
     * @author falm
     * @param
     * @return
     * @Description TODO 覆盖pageHelp的计算总数方法
     * @Date 2021/6/3 16:57
     */
    @Select("SELECT COUNT(courseId) FROM db_course")
    Long getAllCourseVO_COUNT();

    /**
     * 获取当前用户已选课程
     * @param username 用户名
     * @return 课程列表
     */
    @Select("SELECT * FROM courseStudentView WHERE studentId = #{username}")
    List<MyCourseVO> getMyCourseByUsername(String username);
}
