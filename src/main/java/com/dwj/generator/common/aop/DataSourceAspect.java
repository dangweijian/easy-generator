package com.dwj.generator.common.aop;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dwj.generator.config.datasource.DataSourceRouteHolder;
import com.dwj.generator.model.database.TableRequest;
import com.dwj.generator.model.project.ProjectListVo;
import com.dwj.generator.model.project.ProjectRequest;
import com.dwj.generator.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-02 13:05
 **/
@Slf4j
@Aspect
@Component
public class DataSourceAspect {
    @Resource
    private IProjectService projectService;

    @Pointcut("execution(public * com.dwj.generator.controller.DatabaseController.tables(..))")
    public void pointcut(){}
 
    @Before("pointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        try {
            TableRequest request = (TableRequest)joinPoint.getArgs()[0];
            if(request.getProjectId() == null){
                DataSourceRouteHolder.clearDataSourceKey();
            }else {
                //获取项目配置
                ProjectRequest projectRequest = new ProjectRequest();
                projectRequest.setId(request.getProjectId());
                IPage<ProjectListVo> proList = projectService.list(projectRequest);
                if(proList != null && CollectionUtil.isNotEmpty(proList.getRecords())){
                    DataSourceRouteHolder.setDataSourceKey(proList.getRecords().get(0).getDatabaseId().toString());
                }
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