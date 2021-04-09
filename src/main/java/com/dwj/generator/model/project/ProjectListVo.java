package com.dwj.generator.model.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-18 16:52
 **/
@Data
public class ProjectListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String projectName;

    private Long databaseId;

    private String dbName;

    private String rootPath;

    private String author;

    private String description;

    private Date createDate;

    private Date updateDate;
}
