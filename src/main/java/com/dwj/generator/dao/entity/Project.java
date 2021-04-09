package com.dwj.generator.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dwj.generator.common.enums.ProjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dwjian
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目根路径
     */
    private String rootPath;

    /**
     * 数据库连接ID
     */
    private Long databaseId;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 字段前缀
     */
    private String columnPrefix;

    /**
     * 表名映射策略
     */
    private String tableNameStyle;

    /**
     * 字段名映射策略
     */
    private String columnNameStyle;

    /**
     * 项目类型
     */
    private ProjectType projectType;

    /**
     * 使用Lombok
     */
    private boolean useLombok;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;


}
