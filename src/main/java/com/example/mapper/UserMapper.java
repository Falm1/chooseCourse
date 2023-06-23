package com.example.mapper;

import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
* @author Falm
* @description 针对表【db_user】的数据库操作Mapper
* @createDate 2023-06-22 10:19:12
* @Entity com.example.entity.domain.DbUser
*/
@Mapper
public interface UserMapper {

    /**
     * 根据用户名获取用户登录信息
     * @param username 用户名
     * @return 校验用户信息
     */
    @Select("SELECT username, password, role, createUser, createTime, modifyUser, modifyTime FROM db_user WHERE username = #{username}")
    AuthUser getUserByUsername(String username);

    /**
     * 重置密码
     * @param username 用户名
     * @param password 新密码
     * @return 是否更新成功
     */
    @Update("UPDATE db_user SET password = #{password} where username = #{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据学生Id获取他们选的课程Id
     * @param studentId 学生Id
     * @return 课程Id列表
     */
    @Select("select courseId from db_SC WHERE studentId = #{studentId} and isDelete = 0")
    Set<String> getCourseIdByStudentId(String studentId);

    /**
     * 插入用户
     * @param user
     */
    @Insert("insert into db_user values (#{username}, #{password},#{role}, #{createUser},  #{createTime},#{modifyUser}, #{modifyTime}, #{isDelete})")
    void addUser(AuthUser user);

    /**
     * 根据课程号获取选择改课学生的信息
     * @param courseId 课程号
     * @return
     */
    @Results({@Result( column = "studentId", property = "username"),
            @Result(column = "studentName", property = "name")})
    @Select("select * from studentWithCourse where courseId = #{courseId}")
    List<UserVO> getStudentByCourseId(String courseId);
}




