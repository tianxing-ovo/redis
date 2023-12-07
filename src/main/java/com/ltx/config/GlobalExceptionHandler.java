package com.ltx.config;


import io.github.tianxingovo.common.R;
import io.github.tianxingovo.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 集中处理所有异常,将异常信息返回给前端(json格式)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CustomException.class)
    public R handleAccessDeniedException(CustomException e) {
        int code = e.getCode();
        log.error("错误信息:{},异常类型:{}", e.getMessage(), e.getClass());
        return R.error(code, e.getMessage());
    }


    /**
     * 处理未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error("错误信息:{},异常类型:{}", e.getMessage(), e.getClass());
        return R.error(203, e.getMessage());
    }
}

