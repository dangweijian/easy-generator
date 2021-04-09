package com.dwj.generator.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.dwj.generator.common.utils.SpringUtil;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationListener<ApplicationReadyEvent> {

    private static DataSource defaultDataSource;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        //获取多数据源配置留空，待项目启动完成在全部注册
        mpv.addPropertyValue("targetDataSources", new HashMap<>());
        registry.registerBeanDefinition("dataSource", beanDefinition);
        log.info("multiple dataSource beReady !!!");
    }

    @Override
    public void setEnvironment(Environment environment) {
        // 读取主数据源
        Binder binder = new Binder(ConfigurationPropertySources.get(environment));
        BindResult<Properties> bindResult = binder.bind("spring.datasource", Properties.class);
        Properties properties = bindResult.get();
        defaultDataSource = buildDataSource(properties);
    }

    public DataSource buildDataSource(Properties properties) {
        try {
            DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            log.info("main dataSource beReady !!!");
            DynamicDataSource.setMainDataSource(dataSource);
            return dataSource;
        } catch (Exception e) {
            log.error("main dataSource init fail !!!", e);
        }
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //将缓存的DataSourceMap赋值到TargetDataSources
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        dynamicDataSource.setTargetDataSources(DynamicDataSource.getDataSourceMap());
        //获取所有数据连接配置
        IDatabaseService databaseService = SpringUtil.getBean(IDatabaseService.class);
        List<Database> databaseList = databaseService.list();
        DynamicDataSource.batchInit(databaseList, true);
    }
}