package com.example.entity.Request.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePwdRequest implements Serializable {
    /**
     * 用户账户，分别为学生的学号，老师的教职工号以及管理员的特殊账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 确认面膜
     */
    private String checkPassword;
}
