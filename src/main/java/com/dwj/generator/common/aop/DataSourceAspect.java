package com.dwj.generator.common.aop;

import com.dwj.generator.config.datasource.DataSourceRouteHolder;
import com.dwj.generator.model.database.TableRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-02 13:05
 **/
@Slf4j
@Aspect
@Component
public class DataSourceAspect {
    @Pointcut("execution(public * com.dwj.generator.controller.DatabaseController.tables(..))")
    public void pointcut(){}
 
    @Before("pointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        try {
            TableRequest request = (TableRequest)joinPoint.getArgs()[0];
            if(request.getDatabaseId() != null){
                DataSourceRouteHolder.setDataSourceKey(request.getDatabaseId().toString());
            }else {
                DataSourceRouteHolder.clearDataSourceKey();
            }
        }catch (Exception e){
            log.error("前置处理-数据源切面处理异常", e);
        }
    }

    @After("pointcut()")
    public void commit() {
        try{
            DataSourceRouteHolder.clearDataSourceKey();
        }catch (Exception e){
            log.error("后置处理-数据源切面处理异常", e);
        }
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        DataSourceRouteHolder.clearDataSourceKey();
    }
}