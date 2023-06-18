package com.example.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户脱敏信息
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户账户，分别为学生的学号，老师的教职工号以及管理员的特殊账号
     */
    private String username;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户角色 0-管理员 1-老师 2-学生
     */
    private Integer role;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

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
    private String status;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

}
