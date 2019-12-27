package com.vmock.base.exception;

import com.vmock.base.utils.ContextUtils;
import com.vmock.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author mock
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<Void> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Result.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> notFount(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.error("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error("服务器错误，请联系管理员");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Object businessException(HttpServletRequest request, BusinessException e) {
        log.error(e.getMessage(), e);
        if (ContextUtils.isAjAx(request)) {
            return Result.error(e.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("error/business");
            return modelAndView;
        }
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.error(message);
    }

}
