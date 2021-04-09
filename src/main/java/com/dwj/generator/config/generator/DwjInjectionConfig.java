package com.dwj.generator.config.generator;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.dwj.generator.common.enums.TemplateType;
import com.dwj.generator.model.project.ProjectTemplateDto;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-30 11:49
 **/
@Data
public class DwjInjectionConfig extends InjectionConfig{

    public DwjInjectionConfig(List<ProjectTemplateDto> projectTemplates) {
        if(CollectionUtil.isNotEmpty(projectTemplates)){
            ArrayList<FileOutConfig> fileOutConfigs = new ArrayList<>();
            for (ProjectTemplateDto projectTemplate : projectTemplates) {
                if(verify(projectTemplates, projectTemplate)){
                    fileOutConfigs.add(new DwjFileOutConfig(projectTemplate));
                }
            }
            super.setFileOutConfigList(fileOutConfigs);
        }
    }

    @Override
    public void initMap() {
        if(getMap() == null){
           setMap(new HashMap<>());
        }
        if(getMap().get("temp_directory") == null){
            File tempDirectory = new File(File.listRoots()[File.listRoots().length - 1].getAbsolutePath() + "\\temp_easy_generator");
            if(!tempDirectory.exists()){
                tempDirectory.mkdir();
            }
            getMap().put("temp_directory", tempDirectory);
        }
    }

    /**
     * 文件生成相互依赖关系
     * @param allTemplates
     * @param current
     * @return
     */
    public boolean verify(List<ProjectTemplateDto> allTemplates, ProjectTemplateDto current) {
        TemplateType templateType = current.getTemplateType();
        List<TemplateType> typeList = allTemplates.stream().map(ProjectTemplateDto::getTemplateType).collect(Collectors.toList());
        switch (templateType){
            //必须有Service接口，才生成ServiceImpl类
            case SERVICE_IMPL:
                return typeList.contains(TemplateType.SERVICE);
            //必须有Mapper接口才生成MapperImpl和MapperXml
            case MAPPER_IMPL:
            case MAPPER_XML:
                return typeList.contains(TemplateType.MAPPER);
            default:
                return true;
        }
    }
}
