layui.use(['form', 'miniTab'], function () {
    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer,
        miniTab = layui.miniTab;

    var myTextarea = document.getElementById('code-editor');

    var myCodeMirror = CodeMirror.fromTextArea(myTextarea, {
        mode: "text/x-java", //高亮语言
        lineNumbers: true, //显示行号
        smartIndent: true, //智能缩进
        indentUnit: 4, //智能缩进单位为4个空格长度
        matchBrackets: true, //匹配结束符号，比如"]、}"
        autoCloseBrackets: true, //自动闭合符号
        styleActiveLine: true, //显示选中行的样式

    });

    //模板类型下拉选项
    getAjax(basePath + "/api/template/type", undefined, function (result) {
        if(result.code === 0 && result.data && result.data.length > 0){
            var options = "";
            for (const item of result.data) {
                options += "<option value='"+ item +"'>"+item+"</option>";
            }
            $($('#typeSelect').find('option')).after(options);
            form.render('select', 'editForm');
        }
    }, true);

    //编辑时数据回显
    var id = getJsonRequest("id").id;
    if(id){
        getAjax(basePath + "/api/template/detail/" + id, null, function (result) {
            if(result.code === 0 && result.data){
                //模板类型下拉选中回显
                if(result.data.templateType){
                    $('#typeSelect').find('option[value='+result.data.templateType+']').attr('selected', true);
                }
                //模板内容回显
                if(result.data.templateContent){
                    myCodeMirror.setOption("value", result.data.templateContent);
                }
                form.val('editForm', result.data);
            }
        }, true);
    }

    $('#cancel').click(function () {
        layer.confirm('确定取消？新编辑的内容将会丢失！', function () {
            miniTab.deleteCurrentByIframe();
        });
    })

    //保存
    form.on('submit(saveTemplate)', function (data) {
        var templateContent = myCodeMirror.getValue();
        if(!templateContent || templateContent.trim() === ''){
            layer.msg("请输入模板内容");
            return false;
        }
        data.field.templateContent = templateContent;
        postAjax(basePath + "/api/template/save", data.field, function (result) {
            if(result && result.code === 0){
                var index = layer.alert(result.msg, { title: '提示', closeBtn: 0 }, function () {
                    layer.close(index);
                    reloadTable();
                    miniTab.deleteCurrentByIframe();
                });
            }else if(result && result.msg){
                layer.alert(result.msg, { title: '提示' });
            }else {
                layer.alert("保存失败", { title: '提示' });
            }
        });
        return false;
    });
});


function reloadTable() {
    $(".layuimini-tab .layui-tab-title li", parent.document).each(function (idx, item) {
        if($(item).attr('lay-id').indexOf('page/template/template-list.html') !== -1){
            $(".layuimini-tab .layui-tab-content .layui-tab-item", parent.document).each(function (i, e){
                if(idx === i){
                    $(e).find('iframe')[0].contentWindow.reload();
                }
            })
        }
    });
}