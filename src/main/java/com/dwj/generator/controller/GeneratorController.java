package com.dwj.generator.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.dwj.generator.common.enums.ProjectType;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.config.generator.DwjInjectionConfig;
import com.dwj.generator.config.generator.DwjTemplateEngine;
import com.dwj.generator.model.project.ProjectDetail;
import com.dwj.generator.service.IProjectService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-29 16:37
 **/
@RestController
@RequestMapping("/api/generator")
public class GeneratorController {

    @Resource
    private IProjectService projectService;

    @PostMapping("/generate/{projectId}")
    public Response<String> generate(@PathVariable Long projectId, @RequestBody List<String> tables){

        //获取项目配置
        ProjectDetail projectDetail = projectService.detail(projectId);
        String tablePrefix = projectDetail.getTablePrefix();
        if(StrUtil.isNotBlank(tablePrefix) && tablePrefix.endsWith("_")){
            tablePrefix = tablePrefix.substring(0, tablePrefix.length() - 1);
        }
        String fieldPrefix = projectDetail.getColumnPrefix();
        if(StrUtil.isNotBlank(fieldPrefix) && fieldPrefix.endsWith("_")){
            fieldPrefix = fieldPrefix.substring(0, fieldPrefix.length() - 1);
        }

        AutoGenerator mpg = new AutoGenerator();

        //基础配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectDetail.getRootPath());
        gc.setAuthor("easy-generator");
        gc.setOpen(false);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        //数据库配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://"+projectDetail.getDatabase().getHost()+":"+projectDetail.getDatabase().getPort()+"/"+projectDetail.getDatabase().getDbName()+"?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        dsc.setDriverName(projectDetail.getDatabase().getDrive());
        dsc.setUsername(projectDetail.getDatabase().getUsername());
        dsc.setPassword(projectDetail.getDatabase().getPassword());
        mpg.setDataSource(dsc);
        mpg.setCfg(new DwjInjectionConfig(projectDetail.getProjectTemplates()));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.valueOf(projectDetail.getTableNameStyle()));
        strategy.setColumnNaming(NamingStrategy.valueOf(projectDetail.getColumnNameStyle()));
        strategy.setEntityLombokModel(projectDetail.isUseLombok());
        strategy.setRestControllerStyle(ProjectType.SPRING_BOOT.equals(projectDetail.getProjectType()));
        strategy.setInclude(tables.toArray(new String[0]));
        strategy.setTablePrefix(tablePrefix);
        strategy.setFieldPrefix(fieldPrefix);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new DwjTemplateEngine());
        mpg.execute();
        return Response.success("代码生成完毕，请到相应目录查看！");
    }
}
