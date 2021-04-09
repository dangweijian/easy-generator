package com.dwj.generator.model.template;

import com.dwj.generator.common.enums.TemplateType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-26 14:41
 **/
@Data
public class TemplateVo implements Serializable {

    private Long id;

    private String templateName;

    private TemplateType templateType;

    private String remark;

    private Date createDate;

    private Date updateDate;
}
