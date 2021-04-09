package com.dwj.generator.common.response;

/**
 * @ClassName ResultEnum
 * @Description 响应结果枚举
 * @Author dwjian
 * @Date 2019/9/8 21:58
 */
public enum ResultEnum {

    SUCCESS(true, 0, "SUCCESS"),
    FAIL(false, -1, "FAIL"),
    ERROR(false, -1, "系统异常"),

    NO_LOGIN(false,-100,"用户未登录"),
    NO_PERMISSION(false,-200,"无权限访问"),
    ACCOUNT_LOCK(false,-300,"账号已冻结");

    private int code;
    private String msg;
    private boolean isOK;



    ResultEnum(boolean isOK, int code, String msg){
        this.isOK = isOK;
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }
}
