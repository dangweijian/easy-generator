package com.dwj.generator.config.exception;

import com.dwj.generator.common.response.Response;
import com.dwj.generator.common.response.ResultEnum;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: dangweijian
 * @description: 全局异常捕获
 * @create: 2019-12-03 10:24
 **/
@Slf4j
@ControllerAdvice
public class ExceptionCatch {

    /**
     * 线程安全,且构建后不可更改
     */
    private static ImmutableMap<Class<? extends Throwable>, ResultEnum> EXCEPTIONS;

    /**
     * 用于构建ImmutableMap
     */
    private static final ImmutableMap.Builder<Class<? extends Throwable>, ResultEnum> builder = ImmutableMap.builder();

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Response<String> businessExceptionCatch(BusinessException e) {
        log.error("发生业务异常,code:[{}],msg:[{}]", e.getCode(), e.getMsg(), e);
        return Response.error(e.getCode(), e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<String> exceptionCatch(Exception e) {
        log.error("发生系统异常,message:[{}]", e.getMessage(), e);
        //如果ImmutableMap集合为空,构建ImmutableMap
        if (EXCEPTIONS == null || EXCEPTIONS.size() == 0) {
            EXCEPTIONS = builder.build();
        }
        //获取不可预知异常自定义错误码
        if (EXCEPTIONS != null) {
            ResultEnum resultEnum = EXCEPTIONS.get(e.getClass());
            if (resultEnum != null) {
                return Response.error(resultEnum.getCode(), resultEnum.getMsg());
            }
        }
        return Response.error(ResultEnum.ERROR.getCode(), e.getMessage()==null?ResultEnum.ERROR.getMsg():e.getMessage());
    }

    //初始化,不可预知异常自定义错误编码
    static {
//        builder.put(FileNotFoundException.class, ResultEnum.FILE_NOT_EXIST);
    }
}