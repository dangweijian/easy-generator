package com.dwj.generator.controller;


import com.dwj.generator.common.enums.TemplateType;
import com.dwj.generator.common.response.PageResponse;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.dao.entity.Template;
import com.dwj.generator.model.template.TemplateForm;
import com.dwj.generator.model.template.TemplateRequest;
import com.dwj.generator.model.template.TemplateVo;
import com.dwj.generator.service.ITemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dwjian
 * @since 2021-03-26
 */
@RestController
@RequestMapping("/api/template")
public class TemplateController {

    @Resource
    private ITemplateService templateService;

    @GetMapping("/list")
    public PageResponse<TemplateVo> list(TemplateRequest templateRequest) {
        return PageResponse.success(templateService.list(templateRequest));
    }

    @PostMapping("/save")
    public Response<Boolean> add(@RequestBody TemplateForm templateForm) {
        boolean save = templateService.save(templateForm);
        if(save){
            return Response.success("保存成功");
        }
        return Response.fail("保存失败");
    }

    @GetMapping("/detail/{id}")
    public Response<Template> detail(@PathVariable("id") Long id) {
        return Response.success(templateService.detail(id));
    }

    @PostMapping("/delete/{id}")
    public Response<Boolean> delete(@PathVariable("id") Long id) {
        return Response.success(templateService.delete(id));
    }

    @PostMapping("/batch/delete")
    public Response<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return Response.success(templateService.batchDelete(ids));
    }

    @GetMapping("/type")
    public Response<TemplateType[]> type() {
        return Response.success(TemplateType.values());
    }
}
