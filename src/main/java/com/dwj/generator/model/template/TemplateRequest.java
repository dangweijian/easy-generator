package com.dwj.generator.model.template;

import com.dwj.generator.common.annotation.Condition;
import com.dwj.generator.common.enums.ConditionType;
import com.dwj.generator.config.mybatis.plus.QueryCriteria;
import com.dwj.generator.dao.entity.Template;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-26 14:41
 **/
@Data
public class TemplateRequest extends QueryCriteria<Template, TemplateVo> {

    @Condition(type = ConditionType.LIKE)
    private String templateName;

    protected  Class<TemplateVo> getVoClass(){
        return TemplateVo.class;
    }
}
