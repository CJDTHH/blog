package com.guking.blog.hander;

import com.guking.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // 对加了Controller的注解进行拦截 AOP实现
public class AllExceptHander {
    // 统一异常处理
    // 进行异常处理，处理Exception,class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
