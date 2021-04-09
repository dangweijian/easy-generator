package com.dwj.generator.model.project;

import com.dwj.generator.common.enums.ProjectType;
import com.dwj.generator.model.database.DatabaseVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-18 16:52
 **/
@Data
public class ProjectDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String projectName;

    private String rootPath;

    private String author;

    private String description;

    private String tablePrefix;

    private String columnPrefix;

    private String tableNameStyle;

    private String columnNameStyle;

    private ProjectType projectType;

    private boolean useLombok;

    private DatabaseVo database;

    private List<ProTemplateDto> templates;
}
