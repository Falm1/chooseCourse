package com.example.ex;

import lombok.Data;


public enum ErrorCode {

    SYSTEM_ERROR(4000, "系统错误"),
    PARAMS_ERROR(4001,"参数错误"),
    PARAMS_NULL(4002, "参数为空"),
    NO_AUTH(4003, "用户无权限");


    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
