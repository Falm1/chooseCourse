package com.example.ex;

public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
