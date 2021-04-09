package com.dwj.generator.model.database;

import com.dwj.generator.common.enums.DatabaseType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:37
 **/
@Data
public class DatabaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private DatabaseType dbType;

    private String dbName;

    private String host;

    private String port;

    private String drive;

    private String username;

    private boolean showStatus;

    private Boolean status;

    private Date createDate;

    private Date updateDate;
}
