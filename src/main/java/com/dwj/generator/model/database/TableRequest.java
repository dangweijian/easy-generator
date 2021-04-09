package com.dwj.generator.model.database;

import com.dwj.generator.config.mybatis.plus.QueryCriteria;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-01 17:33
 **/
@Data
public class TableRequest extends QueryCriteria<DbTable, DbTable> {

    private Long projectId;

    private Long databaseId;

    private String tableName;
}
