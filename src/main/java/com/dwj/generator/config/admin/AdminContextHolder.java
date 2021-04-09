package com.dwj.generator.config.admin;

import com.dwj.generator.dao.entity.Admin;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-08 14:28
 **/
public class AdminContextHolder {

    private static final ThreadLocal<Admin> contextHolder = new ThreadLocal<>();

    public static void setAdminContext(Admin admin) {
        contextHolder.set(admin);
    }

    public static Admin getAdminContext() {
        return contextHolder.get();
    }

    public static void clearAdminContext() {
        contextHolder.remove();
    }
}
