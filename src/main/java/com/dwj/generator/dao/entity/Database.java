package com.dwj.generator.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dwj.generator.common.enums.DatabaseType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:37
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_database")
public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private DatabaseType dbType;

    private String dbName;

    private String host;

    private String port;

    private String drive;

    private String username;

    private String password;

    private Boolean status;

    private Date createDate;

    private Date updateDate;
}
