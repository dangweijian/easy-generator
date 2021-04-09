package com.dwj.generator.model.project;

import com.dwj.generator.common.enums.TemplateType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-25 11:10
 **/
@Data
public class ProTemplateDto implements Serializable {

    private Long projectId;

    private Long templateId;

    private String templateName;

    private TemplateType templateType;

    private String packageName;

    private String outputPath;
}
