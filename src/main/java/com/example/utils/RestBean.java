package com.example.utils;

public class RestBean<T> {

    private int code;
    private String message;

    private String description;
    private T data;


    public RestBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestBean(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestBean(int code, String message, String description){
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 成功不携带数据
     *
     * @param code 状态码
     * @param message 返回消息
     * @return 封装RestBean
     */
    public static <Void> RestBean<Void> success(String message){
        return new RestBean<>(200, message);
    }

    /**
     * 成功携带数据
     *
     * @param code 状态码
     * @param message 返回消息
     * @param data 携带数据
     * @return 封装RestBean
     */
    public static <T> RestBean<T> success(String message, T data){
        return new RestBean<>(200, message, data);
    }

    /**
     * 失败返回，不需要详细错误信息
     *
     * @param code 状态码
     * @param message 错误信息
     * @return 封装RestBean
     */
    public static <Void> RestBean<Void> failure(int code, String message){
        return new RestBean<>(code, message);
    }

    /**
     * 失败返回，需要详细错误信息
     *
     * @param code 状态码
     * @param message 错误信息
     * @param description 详细错误信息
     * @return 封装RestBean
     */
    public static <Void> RestBean<Void> failure(int code, String message, String description){
        return new RestBean<>(code, message, description);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
