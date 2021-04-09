package com.dwj.generator.controller;


import com.dwj.generator.common.response.PageResponse;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.model.project.ProjectDetailVo;
import com.dwj.generator.model.project.ProjectForm;
import com.dwj.generator.model.project.ProjectListVo;
import com.dwj.generator.model.project.ProjectRequest;
import com.dwj.generator.service.IProjectService;
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
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Resource
    private IProjectService projectService;

    @GetMapping("/list")
    public PageResponse<ProjectListVo> list(ProjectRequest projectRequest) {
        return PageResponse.success(projectService.list(projectRequest));
    }

    @GetMapping("/detail/{id}")
    public Response<ProjectDetailVo> detail(@PathVariable("id") Long id) {
        return Response.success(projectService.voDetail(id));
    }

    @PostMapping("/save")
    public Response<Boolean> add(@RequestBody ProjectForm projectForm) {
        boolean save = projectService.save(projectForm);
        if(save){
            return Response.success("保存成功");
        }
        return Response.fail("保存失败");
    }

    @PostMapping("/delete/{id}")
    public Response<Boolean> delete(@PathVariable("id") Long id) {
        return Response.success(projectService.delete(id));
    }

    @PostMapping("/batch/delete")
    public Response<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return Response.success(projectService.batchDelete(ids));
    }

    @GetMapping("/option")
    public PageResponse<ProjectListVo> option(ProjectRequest projectRequest) {
        return PageResponse.success(projectService.option(projectRequest));
    }
}
