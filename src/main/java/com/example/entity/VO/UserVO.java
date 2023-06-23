package com.example.entity.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO  implements Serializable {
    /**
     *
     */
    private String username;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 入校时间
     */
    private String grade;

    /**
     * 学院
     */
    private String institute;

    /**
     * 专业
     */
    private String major;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 0-在校 1-正常离校 2-异常离校
     */
    private Integer status;

    /**
     * 用户角色 0-管理员 1-老师 2-学生
     */
    private Integer role;
}
