package com.dwj.generator.config.datasource;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-01 15:31
 **/
public class DataSourceRouteHolder {

    private static final ThreadLocal<String> routeHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        routeHolder.set(key);
    }

    public static String getDataSourceKey() {
        return routeHolder.get();
    }

    public static void clearDataSourceKey() {
        routeHolder.remove();
    }
}
