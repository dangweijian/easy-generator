package com.dwj.generator.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwj.generator.common.response.PageResponse;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.common.response.ResultEnum;
import com.dwj.generator.common.utils.ConnectionTestHelper;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.model.database.DatabaseForm;
import com.dwj.generator.model.database.DatabaseRequest;
import com.dwj.generator.model.database.DatabaseVo;
import com.dwj.generator.model.database.DbTable;
import com.dwj.generator.model.database.TableRequest;
import com.dwj.generator.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-17 14:10
 **/
@Slf4j
@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Resource
    private IDatabaseService databaseService;
    @Resource
    private ConnectionTestHelper connectionTestHelper;

    @GetMapping("/list")
    public PageResponse<DatabaseVo> list(DatabaseRequest databaseRequest) {
        return PageResponse.success(databaseService.list(databaseRequest));
    }

    @GetMapping("/option")
    public PageResponse<DatabaseVo> option(DatabaseRequest databaseRequest) {
        return PageResponse.success(databaseService.option(databaseRequest));
    }

    @GetMapping("/detail/{id}")
    public Response<Database> detail(@PathVariable("id") Long id) {
        return Response.success(databaseService.detail(id));
    }

    @PostMapping("/delete/{id}")
    public Response<Boolean> delete(@PathVariable("id") Long id) {
        return Response.success(databaseService.delete(id));
    }

    @PostMapping("/batch/delete")
    public Response<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return Response.success(databaseService.batchDelete(ids));
    }

    @PostMapping("/save")
    public Response<Boolean> add(@RequestBody DatabaseForm databaseForm) {
        boolean save = databaseService.save(databaseForm);
        if(save){
            return Response.success("保存成功");
        }
        return Response.fail("保存失败");
    }

    @PostMapping("/connect")
    public Response<Boolean> connection(@RequestBody DatabaseForm databaseForm) {
        Database database = new Database();
        BeanUtils.copyProperties(databaseForm, database);
        boolean connection = connectionTestHelper.connect(database, false);
        if(connection){
            return Response.success(ResultEnum.SUCCESS.getCode(), "连接成功", true);
        }else {
            return Response.fail("连接失败");
        }
    }

    @GetMapping("/tables")
    public PageResponse<DbTable> tables(TableRequest tableRequest) {
        if(tableRequest.getProjectId() == null){
            return PageResponse.success(new Page<>());
        }
        try{
            IPage<DbTable> tableList = databaseService.getTableList(tableRequest);
            if(CollectionUtil.isNotEmpty(tableList.getRecords())){
                for (DbTable record : tableList.getRecords()) {
                    record.setProjectId(tableRequest.getProjectId());
                }
            }
            return PageResponse.success(tableList);
        }catch (Exception e){
            log.error("获取数据表异常", e);
            return PageResponse.fail("查询异常，请检查项目连接配置！", null);
        }
    }
}
