package com.dwj.generator.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Response
 * @Description success代表响应成功  fail代表主动响应失败  error代表系统异常
 * @Author dwjian
 * @Date 2019/9/8 21:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {

    private int code;
    private String msg;
    private T data;
    private boolean isOK;

    public Response(Response<T> response) {
        this.code = response.getCode();
        this.msg = response.getMsg();
        this.data = response.getData();
        this.isOK = response.isOK();
    }

    public static <T> Response<T> success(){
        return success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    public static <T> Response<T> success(String msg){
        return success(ResultEnum.SUCCESS.getCode(), msg, null);
    }

    public static <T> Response<T> success(T data){
        return success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> Response<T> success(int code, T data){
        return success(code, ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> Response<T> success(int code, String msg, T data){
        return getResponse(code, msg, data, true);
    }



    public static <T> Response<T> fail(String msg) {
        return fail(ResultEnum.FAIL.getCode(), msg);
    }

    public static <T> Response<T> fail(T data) {
        return fail(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg(), data);
    }

    public static <T> Response<T> fail(ResultEnum resultEnum) {
        return fail(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static <T> Response<T> fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> Response<T> fail(int code, String msg, T data) {
        return getResponse(code, msg, data, false);
    }

    public static <T> Response<T> error(String msg) {
        return error(ResultEnum.ERROR.getCode(), msg);
    }

    public static <T> Response<T> error(T data) {
        return error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), data);
    }

    public static <T> Response<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> Response<T> error(int code, String msg, T data) {
        return getResponse(code, msg, data, false);
    }

    private static <T> Response<T> getResponse(int code, String msg, T data, boolean isSuccess){
        return new Response<>(code, msg, data, isSuccess);
    }
}
