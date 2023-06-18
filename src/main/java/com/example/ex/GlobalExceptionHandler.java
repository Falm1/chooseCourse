package com.example.ex;

import com.example.utils.RestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public RestBean<Void> handlerBusiness(BusinessException e){
        log.error("BusinessException", e);
        return RestBean.failure(e.getErrorCode().getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public RestBean<Void> handlerRunTime(RuntimeException e){
        log.error("RuntimeException", e);
        return RestBean.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }

}
