package com.example.mapper;

import com.example.entity.domain.AuthUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    @Select("select * from db_user where username = #{username} AND isDelete = 0")
    AuthUser getUserByUsername(String username);

    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("INSERT INTO " +
            "   db_user(username, password, avatar, " +
            "           role, name, gender, grade, " +
            "           institute, major, phone, " +
            "           email, status, createUser, " +
            "           createTime, modifyUser, modifyTime, isDelete) " +
            "values " +
            "   (#{username}, #{password}, #{avatar}, " +
            "    #{role}, #{name}, #{gender}, #{grade}," +
            "    #{institute}, #{major}, #{phone}," +
            "    #{email}, #{status}, #{createUser}," +
            "    #{createTime}, #{modifyUser}, #{modifyTime}, #{isDelete})")
    int insertUser(AuthUser user);

    @Update("UPDATE db_user SET password = #{password} where username = #{username} AND isDelete = 0")
    boolean updatePassword(@Param("username") String username, @Param("password") String password);
}
