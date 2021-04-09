package com.dwj.generator.model.project;

import com.dwj.generator.common.enums.ProjectType;
import lombok.Data;

import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-18 16:57
 **/
@Data
public class ProjectForm {

    private Long id;

    private String projectName;

    private String rootPath;

    private Long databaseId;

    private String tablePrefix;

    private String columnPrefix;

    private String tableNameStyle;

    private String columnNameStyle;

    private ProjectType projectType;

    private boolean useLombok;

    private String description;

    private String author;

    private List<ProTemplateDto> templates;
}
