layui.use(['form', 'table'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laytpl = layui.laytpl;

    table.render({
        elem: '#currentTableId',
        url: basePath + '/api/database/list',
        toolbar: '#toolbar',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: "checkbox", width: 50},
            {field: 'dbName', width: 200, title: '数据库名称'},
            {field: 'dbType', width: 200, title: '数据库类型'},
            {field: 'host', width: 150, title: 'host'},
            {field: 'port', width: 150, title: 'port'},
            {field: 'username', width: 100, title: '用户名'},
            {field: 'status', width: 200, title: '连接状态', toolbar: '#connectionStatus', align: "center"},
            {field: 'createDate', width: 200, title: '创建时间', sort: true, templet :function (e){return createDateFormat(e.createDate);}},
            {field: 'updateDate', width: 200, title: '更新时间', sort: true, templet :function (e){return createDateFormat(e.updateDate);}},
            {title: '操作', minWidth: 150, fixed: "right", toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 10,
        page: true,
        skin: 'line',
        done: function () {
            //刷新checkbox
            form.render('checkbox');
        }
    });

    // 监听搜索操作
    form.on('submit(data-search-btn)', function (data) {
        data.field.showStatus = $('#showStatus').prop('checked');
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
     * 添加||删除
     */
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            var index = layer.open({
                title: '添加数据库',
                type: 2,
                shade: 0.2,
                maxmin:true,
                area: ['600px', '500px'],
                content: basePath + '/page/database/database-edit.html',
            });
        } else if (obj.event === 'delete') {  // 监听删除操作
            var checkStatus = table.checkStatus('currentTableId'), data = checkStatus.data;
            if(data && data.length > 0){
                var ids = data.map(v => v.id);
                layer.confirm('确定删除？', function (index) {
                    postAjax(basePath + "/api/database/batch/delete", ids, function (result) {
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

    //编辑
    table.on('tool(currentTableFilter)', function (obj) {
        if (obj.event === 'edit') {
            getAjax(basePath + "/api/database/detail/" + obj.data.id, null, function (result) {
                if(result && result.code === 0 && result.data){
                    var index = layer.open({
                        title: '编辑数据库',
                        type: 2,
                        shade: 0.2,
                        maxmin:true,
                        area: ['600px', '500px'],
                        content: basePath + '/page/database/database-edit.html',
                        success: function (layero, index) {
                            //窗口打开回显数据
                            window['layui-layer-iframe' + index].dataEcho(result.data);
                        }
                    });
                }else {
                    layer.msg("系统异常!");
                }
            })
            return false;
        } else if (obj.event === 'delete') {
            layer.confirm('确定删除？', function (index) {
                postAjax(basePath + "/api/database/delete/" + obj.data.id, null, function (result) {
                    if(result && result.code === 0 && result.data === true){
                        obj.del();
                        layer.close(index);
                    }else {
                        layer.msg("系统异常!");
                    }
                })
            });
        }
    });

    //保存
    form.on('submit(saveDatabase)', function (data) {
        postAjax(basePath + "/api/database/save", data.field, function (result) {
            if(result && result.code === 0){
                var index = layer.alert(result.msg, { title: '提示', closeBtn: 0 }, function () {
                    layer.close(index);
                    var iframeIndex = parent.layer.getFrameIndex(window.name);
                    window.parent.reload();
                    parent.layer.close(iframeIndex);
                });
            }else if(result && result.msg){
                layer.alert(result.msg, { title: '提示' });
            }else {
                layer.alert("保存失败", { title: '提示' });
            }
        });
        return false;
    });

    //测试连接
    form.on('submit(testConnection)', function (data) {
        postAjax(basePath + "/api/database/connect", data.field, function (result) {
            if(result && result.code === 0){
                var index = layer.alert(result.msg, {icon: 1, closeBtn: 0 }, function () {
                    layer.close(index);
                });
            }else if(result && result.msg){
                layer.alert(result.msg, {icon: 2});
            }else {
                layer.alert("连接失败", {icon: 2});
            }
        });
        return false;
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
