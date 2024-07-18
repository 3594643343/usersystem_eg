package org.example.user_system.handler;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> exception(Exception e) {
        String msg = e.getMessage();
        if (ObjectUtil.isNotEmpty(msg) && msg.contains("default message")) {
            msg = msg.substring(msg.lastIndexOf("default message"));
            msg = msg.substring(msg.lastIndexOf('[') + 1, msg.indexOf(']'));
        }
        log.error("全局异常信息 ex = {}", msg, e);
        return Result.error(msg);
    }
}
