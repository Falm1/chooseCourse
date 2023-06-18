package com.example.constant;

/**
 * 存放用户常量
 */
public interface UserConstant {
    String USER_LOGIN_STATUS = "userLoginStatus";

    /**
     * 用户名正则表达式
     */
    String USERNAME_REGEX = "^[0-9]{10}$";

    /**
     * 密码正则表达式
     */
    String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d).{3,12}$";

    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "USER:LOGIN:STATE";
}
