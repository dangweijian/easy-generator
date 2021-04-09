package com.dwj.generator.model.database;

import com.dwj.generator.common.annotation.Condition;
import com.dwj.generator.common.enums.ConditionType;
import com.dwj.generator.config.mybatis.plus.QueryCriteria;
import com.dwj.generator.dao.entity.Database;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:35
 **/
@Data
public class DatabaseRequest extends QueryCriteria<Database, DatabaseVo> {

    @Condition(type = ConditionType.LIKE)
    private String dbName;

    private boolean showStatus;

    protected Class<DatabaseVo> getVoClass(){
        return DatabaseVo.class;
    }

    public DatabaseRequest() {
    }

    public DatabaseRequest(long page, long limit) {
        super.setPage(page);
        super.setLimit(limit);
    }
}
