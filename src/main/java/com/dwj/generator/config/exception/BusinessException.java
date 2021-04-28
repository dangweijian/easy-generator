package com.dwj.generator.config.exception;

import com.dwj.generator.common.response.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: dangweijian
 * @description: 业务异常
 * @create: 2020-01-01 17:24
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private Integer code;
    private String msg;

    public BusinessException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = ResultEnum.FAIL.getCode();
        this.msg = ResultEnum.FAIL.getMsg();
    }
}