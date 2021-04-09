package com.dwj.generator.model.project;

import com.dwj.generator.common.enums.TemplateType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-30 14:17
 **/
@Data
public class ProjectTemplateDto implements Serializable {

    private Long projectId;

    private Long templateId;

    private String templateName;

    private String templateContent;

    private TemplateType templateType;

    private String filePrefix;

    private String fileSuffix;

    private String packageName;

    private String outputPath;
}
