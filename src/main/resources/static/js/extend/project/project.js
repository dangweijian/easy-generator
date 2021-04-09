layui.use(['form', 'table', 'miniTab'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        layer = layui.layer,
        miniTab = layui.miniTab;

    miniTab.listen();

    table.render({
        elem: '#currentTableId',
        url: basePath + '/api/project/list',
        toolbar: '#toolbar',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: "checkbox", width: 50},
            {field: 'projectName', width: 200, title: '项目名称'},
            {field: 'rootPath', width: 300, title: '根路径'},
            {field: 'dbName', width: 200, title: '数据库'},
            {field: 'description', width: 250, title: '描述'},
            {field: 'author', width: 100, title: '作者'},
            {field: 'createDate', width: 200, title: '创建时间', sort: true, templet :function (e){return createDateFormat(e.createDate);}},
            {field: 'updateDate', width: 200, title: '更新时间', sort: true, templet :function (e){return createDateFormat(e.updateDate);}},
            {title: '操作', minWidth: 150, fixed: "right", toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 10,
        page: true,
        skin: 'line'
    });

    // 监听搜索操作
    form.on('submit(data-search-btn)', function (data) {
        //执行搜索重载
        table.reload('currentTableId', {
            page: {
                curr: 1
            }
            , where: data.field
        }, 'data');

        return false;
    });

    /**
     * 批量删除
     */
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'delete') {  // 监听删除操作
            var checkStatus = table.checkStatus('currentTableId'), data = checkStatus.data;
            if(data && data.length > 0){
                var ids = data.map(v => v.id);
                layer.confirm('确定删除？', function (index) {
                    postAjax(basePath + "/api/project/batch/delete", ids, function (result) {
                        if(result && result.code === 0 && result.data === true){
                            reload();
                            layer.close(index);
                        }else {
                            layer.msg("系统异常!");
                        }
                    })
                });
            }
        }
    });

    //监听表格复选框选择
    table.on('checkbox(currentTableFilter)', function (obj) {
        console.log(obj)
    });

    //删除&编辑
    table.on('tool(currentTableFilter)', function (obj) {
        if (obj.event === 'delete') {
            layer.confirm('确定删除？', function (index) {
                postAjax(basePath + "/api/project/delete/" + obj.data.id, null, function (result) {
                    if(result && result.code === 0 && result.data === true){
                        obj.del();
                        layer.close(index);
                    }else {
                        layer.msg("系统异常!");
                    }
                })
            });
        }else if(obj.event === 'edit'){
            miniTab.openNewTabByIframe({
                href: "page/project/project-edit.html?id=" + obj.data.id,
                title:"编辑项目模板",
            });
        }
    });
});

//表格重载
function reload(data) {
    if(!data || data == ''){
        data = layui.table.checkStatus('currentTableId').data
    }
    layui.table.reload('currentTableId', {
        page: {
            curr: 1
        },
        where: data.field
    }, 'data');
}
