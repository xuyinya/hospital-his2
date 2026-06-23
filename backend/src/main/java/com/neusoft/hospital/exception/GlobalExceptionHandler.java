package com.neusoft.hospital.exception;

import com.neusoft.hospital.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 * <p>
 * 使用 @RestControllerAdvice 注解对所有Controller进行统一异常拦截和处理。
 * 避免异常信息直接暴露给前端，统一返回Result格式的错误响应。
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理静态资源不存在的异常
     * <p>
     * NoResourceFoundException 在请求不存在的静态资源时抛出，
     * 此处只是静默处理，返回"资源不存在"提示，不记录错误日志避免日志污染。
     * </p>
     *
     * @param e 静态资源未找到异常
     * @return 统一错误响应
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFound(NoResourceFoundException e) {
        // 忽略静态资源找不到的错误
        return Result.error("资源不存在");
    }

    /**
     * 处理运行时异常
     * <p>
     * 捕获Service层和Controller层抛出的RuntimeException（如参数校验失败、业务逻辑异常等），
     * 记录错误日志后返回异常消息给前端。
     * </p>
     *
     * @param e 运行时异常
     * @return 统一错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行异常: ", e);
        return Result.error(e.getMessage());
    }

    /**
     * 处理通用异常
     * <p>
     * 兜底捕获所有未被更精确异常处理器匹配的Exception，
     * 为避免敏感信息泄露，统一返回"系统异常，请联系管理员"提示。
     * </p>
     *
     * @param e 通用异常
     * @return 统一错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error("系统异常，请联系管理员");
    }
}
