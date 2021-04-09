package com.dwj.generator.config.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.dwj.generator.model.project.ProjectTemplateDto;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-30 16:07
 **/
@Data
public class DwjFileOutConfig extends FileOutConfig {

    private final ProjectTemplateDto projectTemplate;

    public DwjFileOutConfig(ProjectTemplateDto projectTemplateDto) {
        this.projectTemplate = projectTemplateDto;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        return projectTemplate.getOutputPath() + "\\" + getFileName(tableInfo) + getFileExpandName(tableInfo);
    }

    public String getFileName(TableInfo tableInfo){
        switch (projectTemplate.getTemplateType()){
            case CONTROLLER:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"Controller");
            case SERVICE:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"Service");
            case SERVICE_IMPL:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"ServiceImpl");
            case ENTITY:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"");
            case MAPPER:
            case MAPPER_XML:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"Mapper");
            case MAPPER_IMPL:
                return (StrUtil.isNotBlank(projectTemplate.getFilePrefix())?projectTemplate.getFilePrefix():"") + tableInfo.getEntityName() + (StrUtil.isNotBlank(projectTemplate.getFileSuffix())?projectTemplate.getFileSuffix():"MapperImpl");
            default:
                return null;
        }
    }

    public String getFileExpandName(TableInfo tableInfo){
        switch (projectTemplate.getTemplateType()){
            case CONTROLLER:
            case SERVICE:
            case SERVICE_IMPL:
            case ENTITY:
            case MAPPER:
            case MAPPER_IMPL:
                return StringPool.DOT_JAVA;
            case MAPPER_XML:
                return StringPool.DOT_XML;
            default:
                return null;
        }
    }
}
