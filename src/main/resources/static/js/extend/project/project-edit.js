layui.use(['form', 'tableSelect', 'miniTab'], function () {
    var $ = layui.jquery,
        form = layui.form,
        tableSelect = layui.tableSelect,
        miniTab = layui.miniTab;

    //初始化数据库下拉
    getAjax(basePath + "/api/database/option", {page:1,limit:100}, function (result) {
        if(result.code === 0 && result.data && result.data.length > 0){
            var options = "";
            for (const item of result.data) {
                options += "<option value='"+ item.id +"'>"+item.dbName+"（"+item.host+":"+item.port+"）"+"</option>";
            }
            $($('#dbSelect').find('option')).after(options);
            form.render('select', 'editForm');
        }
    }, true);

    //编辑时数据回显
    var id = getJsonRequest("id").id;
    if(id){
        getAjax(basePath + "/api/project/detail/" + id, null, function (result) {
            if(result.code === 0 && result.data){
                //表前缀开关回显
                if(result.data.tablePrefix && result.data.tablePrefix !== ''){
                    $('#tablePrefixCheckbox').attr('checked', 'checked');
                    $('#tablePrefixInput').removeAttr('disabled');
                }
                //字段缀开关回显
                if(result.data.columnPrefix && result.data.columnPrefix !== ''){
                    $('#columnPrefixCheckbox').attr('checked', 'checked');
                    $('#columnPrefixInput').removeAttr('disabled');
                }
                //数据库下拉选中回显
                if(result.data.database){
                    $('#dbSelect').find('option[value='+result.data.database.id+']').attr('selected', true);
                }
                //使用Lombok下拉选中回显
                if(result.data.useLombok === true){
                    result.data.useLombok = "true"
                }else if(result.data.useLombok === false) {
                    result.data.useLombok = "false"
                }
                //代码模板选择框回显
                if(result.data.templates){
                    $('#selectTemplateInput').attr('ts-selected', result.data.templates.map(v=>v.templateId).join(','));
                    $('#selectTemplateInput').val(result.data.templates.map(v=>v.templateName).join(' | '));
                }
                //代码模板选项回显
                if(result.data.templates){
                    for (const item of result.data.templates) {
                        item.id = item.templateId;
                        if($('.code-template').length > 0){
                            $($('.code-template')[$('.code-template').length - 1]).after(getTemplateHtml(item));
                        }else {
                            $('#selectTemplate').after(getTemplateHtml(item));
                        }
                    }
                }
                form.val('editForm', result.data);
            }
        }, true);
    }

    //模板列表
    tableSelect.render({
        elem: '#selectTemplateInput',
        searchKey: 'templateName',
        checkedKey: 'id',
        searchPlaceholder: '模板名称',
        table: {
            url: basePath + '/api/template/list',
            cols: [[
                { type: 'checkbox' },
                { field: 'id', title: '模板ID', width: 80 },
                { field: 'templateName', title: '模板名称', width: 300 },
                { field: 'templateType', title: '模板类型', width: 153 }
            ]]
        },
        done: function (elem, data) {
            //获取已经选择的模板code
            var tempCodeArr = [];
            $('.code-template').each(function (idx, ele) {
                tempCodeArr.push($(ele).attr('id'));
            });
            //删除取消选择的模板
            var newIds = data.data.map(v=> "temp_" + v.id);
            layui.each(tempCodeArr,function (idx, ele) {
                if($.inArray(ele, newIds) == -1){
                    $('#' + ele).remove();
                }
            })

            //展示模板模块
            var NEWJSON = []
            layui.each(data.data, function (index, item) {
                NEWJSON.push(item.templateName)
                if($('#temp_' + item.id).length == 0){
                    if($('.code-template').length > 0){
                        $($('.code-template')[$('.code-template').length - 1]).after(getTemplateHtml(item));
                    }else {
                        $('#selectTemplate').after(getTemplateHtml(item));
                    }
                }
            });
            elem.val(NEWJSON.join(" | "));
        }
    });

    //修改项目根路径
    $("#rootPath").on('input propertychange',function () {
        if(new RegExp(".*/src/main/java$").test($('#classPath').val())){
            $('#classPath').val($("#rootPath").val() + "/src/main/java");
        }
        if(new RegExp(".*/src/main/resources$").test($('#resourcesPath').val())){
            $('#resourcesPath').val($("#rootPath").val() + "/src/main/resources");
        }
    });

    //选择路径
    $("body").on('click', '.select-path', function () {
        var eleId = $(this).parents('.layui-form-item').find('input').attr('id');
        if(!eleId){
            eleId = $(this).parents('.layui-inline').find('input').attr('id');
            if(!$('#rootPath').val()){
                layer.msg("请先选择项目根路径");
                return;
            }
        }
        var index = layer.open({
            title: '选择路径',
            type: 2,
            shade: 0.2,
            area: ['500px', '350px'],
            content: basePath + '/page/plugin/path-selector.html',
            success: function (layero, index) {
                //窗口初始化
                window['layui-layer-iframe' + index].initParent(eleId, $('#rootPath').val());
            }
        });
    });

    //监听指定开关
    form.on('switch(columnPrefix)', function(data){
        if(this.checked){
            layer.tips('温馨提示：生成的代码中，字段前缀将会被忽略。', data.othis);
            $('#columnPrefixInput').attr('disabled', false);
            $('#columnPrefixInput').attr('lay-verify', 'required');
            $('#columnPrefixInput').attr('lay-reqtext', '请输入字段前缀');
        }else {
            $('#columnPrefixInput').val('');
            $('#columnPrefixInput').attr('lay-verify','');
            $('#columnPrefixInput').attr('lay-reqtext','');
            $('#columnPrefixInput').attr('disabled', true);
        }
    });

    //监听指定开关
    form.on('switch(tablePrefix)', function(data){
        if(this.checked){
            layer.tips('温馨提示：生成的代码中，表前缀将会被忽略。', data.othis);
            $('#tablePrefixInput').attr('disabled', false);
            $('#tablePrefixInput').attr('lay-verify', 'required');
            $('#tablePrefixInput').attr('lay-reqtext', '请输入表前缀');
        }else {
            $('#tablePrefixInput').val('');
            $('#tablePrefixInput').attr('lay-verify','');
            $('#tablePrefixInput').attr('lay-reqtext','');
            $('#tablePrefixInput').attr('disabled', true);
        }
    });

    $('#cancel').click(function () {
        layer.confirm('确定取消？新编辑的内容将会丢失！', function () {
            miniTab.deleteCurrentByIframe();
        });
    })

    //删除代码模板
    $('body').on('click', '.deleteTemplate', function () {
        var delId = $(this).parents('.code-template').attr('id').split('_')[1];
        var $that = $(this);
        layer.confirm('确定删除？', function (index) {
            //已选择的id
            var selectIds = $('#selectTemplateInput').attr('ts-selected').split(',');
            var delIdx = selectIds.indexOf(delId);
            selectIds.splice(delIdx, 1);
            //已选择的名称
            var selectNames = $('#selectTemplateInput').val().split(' | ');
            selectNames.splice(delIdx, 1);
            //从新写入选中的值
            $('#selectTemplateInput').attr('ts-selected',selectIds.join(","));
            $('#selectTemplateInput').val(selectNames.join(" | "));
            //删除模板结构
            $that.parents('.code-template').remove();
            layer.close(index);
        });
    })

    //保存
    form.on('submit(saveProject)', function (data) {
        var tempInfo = []
        $('.code-template').each(function (idx, ele) {
            var tempId = $($(ele).find('input')[0]).attr('temp-id');
            var templateName = $($(ele).find('input')[0]).val();
            var packageName = $($(ele).find('input')[1]).val();
            var outputPath = $($(ele).find('input')[2]).val();
            if(data.field.id){
                tempInfo.push({projectId: data.field.id, templateId: tempId, templateName: templateName, packageName: packageName, outputPath: outputPath});
            }else {
                tempInfo.push({templateId: tempId, templateName: templateName, packageName: packageName, outputPath: outputPath});
            }
        })
        data.field.templates = tempInfo;
        postAjax(basePath + "/api/project/save", data.field, function (result) {
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

function getTemplateHtml(data) {
     return  "<div class='layui-form-item code-template' id='temp_"+ data.id +"'>" +
                   "<div class='layui-inline' style='width: 380px'>" +
                   "    <label class='layui-form-label required'>模板</label>" +
                   "    <div class='layui-input-block'>" +
                   "        <input type='text' disabled='disabled' temp-id='"+ data.id + "' value='"+ data.templateName +"' autocomplete='off' class='layui-input'>" +
                   "    </div>" +
                   "</div>" +
                   "<div class='layui-inline' style='width: 334px'>" +
                   "    <label class='layui-form-label required'>包名</label>" +
                   "    <div class='layui-input-block'>" +
                   "        <input type='text' " + (data.templateType !== "MAPPER_XML"?"lay-verify='required' lay-reqtext='包名不能为空' placeholder='请输入包名'":"") +" value='"+ (data.packageName?data.packageName:"") +"' autocomplete='off' class='layui-input'>" +
                   "    </div>" +
                   "</div>" +
                   "<div class='layui-inline' style='width: 672px'>" +
                       "<label class='layui-form-label required'>输出路径</label>" +
                       "<div class='layui-input-block'>" +
                           "<input type='text' readonly='readonly' id='temp_out_"+ data.id +"' lay-verify='required' lay-reqtext='输出路径不能为空' placeholder='请选择输出路径，注：必须为项目根路径开头' value='"+ (data.outputPath?data.outputPath:"") +"' autocomplete='off' class='layui-input select-path path-select'>" +
                           "<span class='layui-tab-bar select-path' style='height: 36px;line-height: 35px;'><i class='fa fa-folder-open-o' style='margin-top: 12px;color: #666666'></i></span>" +
                       "</div>" +
                   "</div>" +
                   "<div class='layui-inline' style='margin-right: 0px'>" +
                   "    <button type='button' class='layui-btn layui-btn-danger deleteTemplate' style='right:0px;padding: 0 13px'><i class='fa fa-trash-o'></i></button>" +
                   "</div>" +
             "</div>";
}

function selectedPath(eleId, value) {
    var rootPath = $('#rootPath').val();
    if(/^temp_out_/.test(eleId)){
        if(!rootPath){
            layui.layer.msg('请先选择项目根路径');
            return;
        }
        rootPath = rootPath.replaceAll('\\','\\\\');
        if(!new RegExp('^' + rootPath).test(value)){
            layui.layer.open({
                title: '错误',
                content: '输出路径必须以项目根路径开头'
            });
            return;
        }
    }
    if(eleId === 'rootPath'){
        $('.path-select').each(function (idx, ele) {
            if($(ele).val()){
                var temp = $(ele).val().replace(rootPath, '');
                if(temp !== ''){
                    if(/^\\/.test(temp)){
                        if(/\\$/.test(value)){
                            $(ele).val(value + temp.slice(1))
                        }else {
                            $(ele).val(value + temp)
                        }
                    }else {
                        if(/\\$/.test(value)){
                            $(ele).val(value + temp)
                        }else {
                            $(ele).val(value + '\\' + temp)
                        }
                    }
                }else {
                    $(ele).val(value)
                }
            }
        })
    }
    $('#' + eleId).val(value);
}

function getRootPath() {
    return $('#rootPath').val();
}

function reloadTable() {
    $(".layuimini-tab .layui-tab-title li", parent.document).each(function (idx, item) {
        if($(item).attr('lay-id').indexOf('page/project/project-list.html') !== -1){
            $(".layuimini-tab .layui-tab-content .layui-tab-item", parent.document).each(function (i, e){
                if(idx === i){
                    $(e).find('iframe')[0].contentWindow.reload();
                }
            })
        }
    });
}