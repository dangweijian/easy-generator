package com.dwj.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwj.generator.common.utils.ConnectionTestHelper;
import com.dwj.generator.config.datasource.DynamicDataSource;
import com.dwj.generator.config.mybatis.plus.CommonServiceImpl;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.dao.mapper.DatabaseMapper;
import com.dwj.generator.model.database.DatabaseForm;
import com.dwj.generator.model.database.DatabaseRequest;
import com.dwj.generator.model.database.DatabaseVo;
import com.dwj.generator.model.database.DbTable;
import com.dwj.generator.model.database.TableRequest;
import com.dwj.generator.service.IDatabaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:33
 **/
@Service
public class DatabaseServiceImpl extends CommonServiceImpl<DatabaseMapper, Database> implements IDatabaseService {

    @Resource
    private ConnectionTestHelper connectionTestHelper;

    @Override
    public IPage<DatabaseVo> list(DatabaseRequest databaseRequest) {
        IPage<Database> iPage = this.baseMapper.selectPage(new Page<>(databaseRequest.getPage(), databaseRequest.getLimit()), databaseRequest.simpleWrapper());
        if(iPage != null && iPage.getRecords() != null){
            final List<Database> records = iPage.getRecords();
            IPage<DatabaseVo> pageVo = this.convert(iPage, DatabaseVo.class);
            if(databaseRequest.isShowStatus()){
                pageVo.getRecords().forEach(vo -> {
                    for (Database database : records) {
                        if(database.getId().equals(vo.getId())
                                && (database.getStatus() == null || database.getStatus())){
                            vo.setStatus(connectionTestHelper.connect(database, true));
                        }
                    }
                    vo.setShowStatus(databaseRequest.isShowStatus());
                });
            }
            return pageVo;
        }
        return new Page<>();
    }

    @Override
    public IPage<DatabaseVo> option(DatabaseRequest databaseRequest) {
        return this.voPage(databaseRequest);
    }

    @Override
    public IPage<DbTable> getTableList(TableRequest tableRequest) {
        return this.getBaseMapper().getTableList(new Page<>(tableRequest.getPage(), tableRequest.getLimit()), tableRequest);
    }

    @Override
    public boolean save(DatabaseForm databaseForm) {
        Database database = new Database();
        BeanUtils.copyProperties(databaseForm, database);
        database.setStatus(true);
        boolean save = this.saveOrUpdate(database);
        if(save && database.getId() != null){
            DynamicDataSource.init(database, true);
        }
        return save;
    }

    @Override
    public Database detail(Long id) {
        return this.lambdaQuery().eq(Database::getId, id).one();
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }
}
