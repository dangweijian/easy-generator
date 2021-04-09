package com.dwj.generator.model.template;

import com.dwj.generator.common.enums.TemplateType;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-29 10:40
 **/
@Data
public class TemplateForm {

    private Long id;

    private String templateName;

    private TemplateType templateType;

    private String templateContent;

    private String filePrefix;

    private String fileSuffix;

    private String remark;
}
