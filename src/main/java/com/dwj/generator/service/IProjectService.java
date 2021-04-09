package com.dwj.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwj.generator.dao.entity.Project;
import com.dwj.generator.model.project.ProjectDetail;
import com.dwj.generator.model.project.ProjectDetailVo;
import com.dwj.generator.model.project.ProjectForm;
import com.dwj.generator.model.project.ProjectListVo;
import com.dwj.generator.model.project.ProjectRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dwjian
 * @since 2021-03-18
 */
public interface IProjectService extends IService<Project> {

    IPage<ProjectListVo> list(ProjectRequest projectRequest);

    boolean save(ProjectForm projectForm);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    ProjectDetailVo voDetail(Long id);

    ProjectDetail detail(Long id);

    IPage<ProjectListVo> option(ProjectRequest projectRequest);
}
