package com.dwj.generator.model.database;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-31 20:16
 **/
@Data
public class DbTable implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long projectId;

    private String dbName;

    private String engine;

    private String tableName;

    private String remark;

    private Date createDate;
}
