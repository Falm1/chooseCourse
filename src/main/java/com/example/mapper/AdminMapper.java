package com.example.mapper;

import com.example.entity.domain.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author Falm
* @description 针对表【db_admin_info】的数据库操作Mapper
* @createDate 2023-06-22 10:40:39
* @Entity com.example.entity.domain.DbAdminInfo
*/
@Mapper
public interface AdminMapper {

    /**
     * 根据管理员Id查询管理员信息
     * @param adminId 管理员
     * @return 管理员信息
     */
    @Select("select * FROM db_admin_info WHERE adminId = #{adminId} and isDelete = 0")
    Admin getAdminByAdminId(String adminId);
}




