package com.dwj.generator.common.utils;

import com.alibaba.fastjson.JSON;
import com.dwj.generator.common.response.Response;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;


/**
 * @author: dangweijian
 * @description: 构造JSON响应工具类
 * @Author dwjian
 * @Date 2020/7/10 15:51
 **/
@Slf4j
public class JSONResponseUtils {

    /**
     * 使用response输出JSON
     *
     * @param servletResponse
     * @param response
     */
    public static void build(ServletResponse servletResponse, Response<?> response) {
        PrintWriter out = null;
        try {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json");
            out = servletResponse.getWriter();
            out.println(JSON.toJSONString(response));
        } catch (Exception e) {
            log.error(e + "输出JSON出错");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
