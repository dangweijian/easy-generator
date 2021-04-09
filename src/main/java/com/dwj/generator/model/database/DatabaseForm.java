package com.dwj.generator.model.database;

import com.dwj.generator.common.enums.DatabaseType;
import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 18:06
 **/
@Data
public class DatabaseForm {

    private Long id;

    private DatabaseType dbType;

    private String dbName;

    private String host;

    private String port;

    private String drive;

    private String username;

    private String password;
}
