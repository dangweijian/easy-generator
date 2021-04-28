package com.dwj.generator.config.interceptor;

import com.dwj.generator.dao.entity.Admin;
import com.dwj.generator.service.IAdminService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: dangweijian
 * @description: 抽象拦截器
 * @create: 2020-01-19 16:26
 **/
public abstract class AbstractInterceptor {

    protected final static Map<Long, Admin> adminCache = new HashMap<>();

    @Resource
    protected IAdminService adminService;

    /**
     * 拦截根目录
     * @return
     */
    public abstract String pathPatterns();

    /**
     * 排除路径，不会进入拦截器
     * @return
     */
    public abstract Set<String> excludePathPatterns();

    /**
     * 拦截器顺序，从小到大
     * @return
     */
    public abstract int order();

}
