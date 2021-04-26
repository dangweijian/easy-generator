package com.dwj.generator.model.project;

import com.dwj.generator.common.annotation.Condition;
import com.dwj.generator.common.enums.ConditionType;
import com.dwj.generator.config.mybatis.plus.QueryCriteria;
import com.dwj.generator.dao.entity.Project;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-18 16:53
 **/
@Data
public class ProjectRequest extends QueryCriteria<Project, ProjectListVo> {

    @Condition(type = ConditionType.EQ)
    private Long id;

    @Condition(type = ConditionType.LIKE)
    private String projectName;

    public Class<ProjectListVo> getVoClass(){
        return ProjectListVo.class;
    }
}
