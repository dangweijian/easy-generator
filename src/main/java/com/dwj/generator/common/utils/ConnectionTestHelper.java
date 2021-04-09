package com.dwj.generator.common.utils;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.dwj.generator.config.datasource.DynamicDataSource;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 18:57
 **/
@Slf4j
@Component
public class ConnectionTestHelper {

    @Resource
    private IDatabaseService databaseService;

    public boolean connect(Database database, boolean cache){
        DruidPooledConnection connection = null;
        try {
             connection = DynamicDataSource.getConnection(database, cache);
            return connection != null;
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("释放连接异常", e);
                }
            }else {
                if(cache && database.getId() != null){
                    databaseService.lambdaUpdate().eq(Database::getId, database.getId()).set(Database::getStatus, 0).update();
                }
            }
        }
    }
}
