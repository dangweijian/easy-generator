package com.dwj.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.model.database.DatabaseForm;
import com.dwj.generator.model.database.DatabaseRequest;
import com.dwj.generator.model.database.DatabaseVo;
import com.dwj.generator.model.database.DbTable;
import com.dwj.generator.model.database.TableRequest;

import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:32
 **/
public interface IDatabaseService extends IService<Database> {

    IPage<DatabaseVo> list(DatabaseRequest databaseRequest);

    boolean save(DatabaseForm databaseForm);

    Database detail(Long id);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    IPage<DatabaseVo> option(DatabaseRequest databaseRequest);

    IPage<DbTable> getTableList(TableRequest tableRequest);
}
