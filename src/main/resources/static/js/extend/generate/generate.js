layui.use(['form', 'table', 'miniTab'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        layer = layui.layer,
        miniTab = layui.miniTab;

    //获取项目列表
    getAjax(basePath + "/api/project/option", {page:1,limit:100}, function (result) {
        if(result.code === 0 && result.data && result.data.length > 0){
            var options = "";
            for (const item of result.data) {
                options += "<option database-id='"+item.databaseId+"' value='"+ item.id +"'>"+item.projectName+"</option>";
            }
            $($('#projectId').find('option')).after(options);
            form.render('select', 'searchForm');
        }
    })

    table.render({
        elem: '#currentTableId',
        url: basePath + '/api/database/tables',
        toolbar: '#toolbar',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: "checkbox", width: 50},
            {field: 'tableName', width: 250, title: '表名'},
            {field: 'dbName', width: 250, title: '数据库'},
            {field: 'engine', width: 200, title: '引擎'},
            {field: 'remark', width: 300, title: '表注释'},
            {field: 'createDate', width: 200, title: '创建时间', sort: true, templet :function (e){return createDateFormat(e.createDate);}},
            {title: '操作', minWidth: 300, fixed: "right", toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 10,
        page: true,
        skin: 'line'
    });

    // 监听搜索操作
    form.on('submit(data-search-btn)', function (data) {
        var databaseId = $("#projectId").find("option:selected").attr('database-id');
        data.field.databaseId = databaseId;
        //执行搜索重载
        table.reload('currentTableId', {
            page: {
                curr: 1
            }
            , where: data.field
        }, 'data');

        return false;
    });

    //监听表格复选框选择
    table.on('checkbox(currentTableFilter)', function (obj) {
        console.log(obj)
    });

    //单个生成
    table.on('tool(currentTableFilter)', function (obj) {
        if (obj.event === 'generate') {
            layer.confirm('确定生成？', function (index) {
                postAjax(basePath + "/api/generator/generate/" + obj.data.projectId, [obj.data.tableName], function (result) {
                    if(result && result.code === 0){
                        layer.msg(result.msg);
                        layer.close(index);
                    }else {
                        layer.msg("系统异常!");
                    }
                })
            });
        }
    });

    //批量生成
    $('body').on('click', '#batchGenerate', function () {
        //校验复选框
        var checkStatus = table.checkStatus('currentTableId'), data = checkStatus.data;
        if(!data || data.length === 0){
            layer.msg("请至少选中一个表！");
            return;
        }
        var selectedTables = data.map(v => v.tableName);
        layer.confirm('确定批量生成？', function (index) {
            var projectId = $("#projectId").find("option:selected").val();
            postAjax(basePath + "/api/generator/generate/" + projectId, selectedTables, function (result) {
                if(result && result.code === 0){
                    layer.msg(result.msg);
                    layer.close(index);
                }else {
                    layer.msg("系统异常!");
                }
            })
        });
    })

    /**
     * 查看项目配置
     */
    table.on('toolbar(currentTableFilter)', function (obj) {
        var projectId = $("#projectId").find("option:selected").val();
        var databaseId = $("#projectId").find("option:selected").attr('database-id');
        if (obj.event === 'projectConfig' && projectId &&projectId !== '') {
            miniTab.openNewTabByIframe({
                href: "page/project/project-edit.html?id=" + projectId,
                title:"编辑项目模板",
            });
        }else if(obj.event === 'databaseConfig' && databaseId && databaseId !== ''){
            getAjax(basePath + "/api/database/detail/" + databaseId, null, function (result) {
                if(result && result.code === 0 && result.data){
                    var index = layer.open({
                        title: '查看连接配置',
                        type: 2,
                        shade: 0.2,
                        maxmin:true,
                        area: ['600px', '500px'],
                        content: basePath + '/page/database/database-edit.html',
                        success: function (layero, index) {
                            //窗口打开回显数据
                            window['layui-layer-iframe' + index].dataEcho(result.data, true);
                        }
                    });
                }else {
                    layer.msg("系统异常!");
                }
            })
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
