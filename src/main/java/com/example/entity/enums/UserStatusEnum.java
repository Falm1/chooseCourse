package com.example.entity.enums;

import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;
import org.springframework.security.core.userdetails.User;

public enum UserStatusEnum {

    ADMIN(0, "管理员"),
    TEACHER(1, "老师"),
    STUDENT(2, "学生");

    public static UserStatusEnum getUserStatusEnumByRole(Integer role){
        if(role == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL);
        }
        UserStatusEnum[] roles = UserStatusEnum.values();
        for(UserStatusEnum userStatusEnum : roles){
            if(userStatusEnum.getRole() == role){
                return userStatusEnum;
            }
        }
        return null;
    }
    private int role;
    private String text;

    UserStatusEnum(int role, String text) {
        this.role = role;
        this.text = text;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
