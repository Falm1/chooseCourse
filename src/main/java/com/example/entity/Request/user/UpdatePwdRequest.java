package com.example.entity.Request.user;

import lombok.Data;

@Data
public class UpdatePwdRequest {

    /**
     * 用户账户，分别为学生的学号，老师的教职工号以及管理员的特殊账号
     */
    private String username;

    /**
     * 用户新密码
     */
    private String password;

    /**
     * 用户确认密码
     */
    private String checkPassword;
}
