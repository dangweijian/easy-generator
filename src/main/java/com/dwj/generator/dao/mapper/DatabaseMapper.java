package com.dwj.generator.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwj.generator.config.mybatis.plus.CommonMapper;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.model.database.DbTable;
import com.dwj.generator.model.database.TableRequest;
import org.apache.ibatis.annotations.Param;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:52
 **/
public interface DatabaseMapper extends CommonMapper<Database> {

    IPage<DbTable> getTableList(Page<DbTable> tPage, @Param("criteria") TableRequest criteria);
}
