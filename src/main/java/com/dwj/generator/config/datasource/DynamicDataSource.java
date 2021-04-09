package com.dwj.generator.config.datasource;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.dwj.generator.common.utils.SpringUtil;
import com.dwj.generator.dao.entity.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_DRIVERCLASSNAME;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_INITIALSIZE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MAXACTIVE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MAXWAIT;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MINEVICTABLEIDLETIMEMILLIS;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MINIDLE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_PASSWORD;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_POOLPREPAREDSTATEMENTS;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_REMOVEABANDONED;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_TESTONBORROW;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_TESTONRETURN;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_TESTWHILEIDLE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_URL;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_USERNAME;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_VALIDATIONQUERY_TIMEOUT;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource{

    private static final String INITIAL_SIZE = "1";
    private static final String MAX_ACTIVE = "5";
    private static final String MIN_IDLE = "1";
    private static final String MAX_WAIT = "3000";
    private static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "10000";
    private static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "60000";
    private static final String TEST_WHILE_IDLE = "true";
    private static final String TEST_ON_BORROW = "false";
    private static final String TEST_ON_RETURN = "false";
    private static final String REMOVE_ABANDONED = "true";
    private static final String MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = "20";
    private static final String VALIDATIONQUERY_TIMEOUT = "5000";
    private static final String PROP_VALIDATIONQUERY = "select * from mysql limit 1";

    private static DruidDataSource mainDataSource;
    private static Map<Object, Object> dataSourceMap = new HashMap<>();

    public static Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    public static void setMainDataSource(DruidDataSource mainDataSource) {
        DynamicDataSource.mainDataSource = mainDataSource;
    }

    public static DruidDataSource getMainDataSource() {
        return mainDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceRouteHolder.getDataSourceKey();
    }

    public static DruidDataSource init(Database database, boolean cache) {
        HashMap<String, String> param = new HashMap<>();
        param.put(PROP_DRIVERCLASSNAME, database.getDrive());
        param.put(PROP_URL, "jdbc:mysql://" + database.getHost() +":"+ database.getPort() + "/"+ database.getDbName() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
        param.put(PROP_USERNAME, database.getUsername());
        param.put(PROP_PASSWORD, database.getPassword());
        param.put(PROP_INITIALSIZE, INITIAL_SIZE);
        param.put(PROP_MAXACTIVE, MAX_ACTIVE);
        param.put(PROP_MINIDLE, MIN_IDLE);
        param.put(PROP_MAXWAIT, MAX_WAIT);
        param.put(PROP_TIMEBETWEENEVICTIONRUNSMILLIS, TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        param.put(PROP_MINEVICTABLEIDLETIMEMILLIS, MIN_EVICTABLE_IDLE_TIME_MILLIS);
        param.put(PROP_TESTWHILEIDLE, TEST_WHILE_IDLE);
        param.put(PROP_TESTONBORROW, TEST_ON_BORROW);
        param.put(PROP_TESTONRETURN, TEST_ON_RETURN);
        param.put(PROP_POOLPREPAREDSTATEMENTS, MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE);
        param.put(PROP_REMOVEABANDONED, REMOVE_ABANDONED);
        param.put(PROP_VALIDATIONQUERY, null);
        param.put(PROP_VALIDATIONQUERY_TIMEOUT, VALIDATIONQUERY_TIMEOUT);
        try {
            DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(param);
            dataSource.setBreakAfterAcquireFailure(true);
            dataSource.setConnectionErrorRetryAttempts(0);
            if(cache && database.getId() != null){
                dataSourceMap.put(database.getId().toString(), dataSource);
                refreshDynamicDatabase();
            }
            return dataSource;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<DruidDataSource> batchInit(List<Database> databaseList, boolean cache) {
        if(CollectionUtil.isNotEmpty(databaseList)){
            List<DruidDataSource> druidDataSourceList = new ArrayList<>();
            for (Database database : databaseList) {
                HashMap<String, String> param = new HashMap<>();
                param.put(PROP_DRIVERCLASSNAME, database.getDrive());
                param.put(PROP_URL, "jdbc:mysql://" + database.getHost() +":"+ database.getPort() + "/"+ database.getDbName() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
                param.put(PROP_USERNAME, database.getUsername());
                param.put(PROP_PASSWORD, database.getPassword());
                param.put(PROP_INITIALSIZE, INITIAL_SIZE);
                param.put(PROP_MAXACTIVE, MAX_ACTIVE);
                param.put(PROP_MINIDLE, MIN_IDLE);
                param.put(PROP_MAXWAIT, MAX_WAIT);
                param.put(PROP_TIMEBETWEENEVICTIONRUNSMILLIS, TIME_BETWEEN_EVICTION_RUNS_MILLIS);
                param.put(PROP_MINEVICTABLEIDLETIMEMILLIS, MIN_EVICTABLE_IDLE_TIME_MILLIS);
                param.put(PROP_TESTWHILEIDLE, TEST_WHILE_IDLE);
                param.put(PROP_TESTONBORROW, TEST_ON_BORROW);
                param.put(PROP_TESTONRETURN, TEST_ON_RETURN);
                param.put(PROP_POOLPREPAREDSTATEMENTS, MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE);
                param.put(PROP_REMOVEABANDONED, REMOVE_ABANDONED);
                param.put(PROP_VALIDATIONQUERY, null);
                param.put(PROP_VALIDATIONQUERY_TIMEOUT, VALIDATIONQUERY_TIMEOUT);
                try {
                    DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(param);
                    dataSource.setBreakAfterAcquireFailure(true);
                    dataSource.setConnectionErrorRetryAttempts(0);
                    if(cache && database.getId() != null){
                        dataSourceMap.put(database.getId().toString(), dataSource);
                    }
                    druidDataSourceList.add(dataSource);
                } catch (Exception e) {
                    log.error("datasource init fail !!! --->" + database.getDbName());
                }
            }
            refreshDynamicDatabase();
            return druidDataSourceList;
        }
        return null;
    }

    public static DruidPooledConnection getConnection(Database database, boolean cache){
        if(cache){
            DruidDataSource dataSource = (DruidDataSource) dataSourceMap.get(database.getId().toString());
            try{
                if(dataSource == null){
                    synchronized (DynamicDataSource.class){
                        DruidDataSource targetDataSource = (DruidDataSource) dataSourceMap.get(database.getId().toString());
                        if(targetDataSource != null){
                            return targetDataSource.getConnection();
                        }else {
                            dataSource = init(database, cache);
                            if(dataSource != null){
                                return dataSource.getConnection();
                            }
                        }
                    }
                }else {
                    return dataSource.getConnection();
                }
            }catch (SQLException e){
                return null;
            }
        }else {
            synchronized (DynamicDataSource.class){
                try {
                    return Objects.requireNonNull(init(database, cache)).getConnection();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static void refreshDynamicDatabase(){
        SpringUtil.getBean(DynamicDataSource.class).afterPropertiesSet();
    }
}