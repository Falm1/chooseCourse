package com.example.entity.enums;

import com.example.ex.BusinessException;
import com.example.ex.ErrorCode;

public enum UserRoleEnum {

    ADMIN(0, "管理员"),
    TEACHER(1, "老师"),
    STUDENT(2, "学生");

    public int getRole() {
        return role;
    }
    public String getText() {
        return text;
    }

    public static UserRoleEnum getUserRoleEnumByRole(Integer role){
        if(role == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL);
        }
        UserRoleEnum[] roles = UserRoleEnum.values();
        for(UserRoleEnum userRoleEnum : roles){
            if(userRoleEnum.getRole() == role){
                return userRoleEnum;
            }
        }
        return null;
    }
    private int role;
    private String text;

    UserRoleEnum(int role, String text) {
        this.role = role;
        this.text = text;
    }
}
