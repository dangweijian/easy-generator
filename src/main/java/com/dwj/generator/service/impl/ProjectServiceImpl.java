package com.dwj.generator.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwj.generator.config.mybatis.plus.CommonServiceImpl;
import com.dwj.generator.dao.entity.Database;
import com.dwj.generator.dao.entity.Project;
import com.dwj.generator.dao.entity.ProjectTemplate;
import com.dwj.generator.dao.entity.Template;
import com.dwj.generator.dao.mapper.ProjectMapper;
import com.dwj.generator.model.database.DatabaseVo;
import com.dwj.generator.model.project.ProTemplateDto;
import com.dwj.generator.model.project.ProjectDetail;
import com.dwj.generator.model.project.ProjectDetailVo;
import com.dwj.generator.model.project.ProjectForm;
import com.dwj.generator.model.project.ProjectListVo;
import com.dwj.generator.model.project.ProjectRequest;
import com.dwj.generator.model.project.ProjectTemplateDto;
import com.dwj.generator.service.IDatabaseService;
import com.dwj.generator.service.IProjectService;
import com.dwj.generator.service.IProjectTemplateService;
import com.dwj.generator.service.ITemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dwjian
 * @since 2021-03-18
 */
@Service
public class ProjectServiceImpl extends CommonServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    private IProjectTemplateService projectTemplateService;

    @Resource
    private IDatabaseService databaseService;

    @Resource
    private ITemplateService templateService;

    @Override
    public IPage<ProjectListVo> list(ProjectRequest projectRequest) {
        IPage<Project> iPage = this.baseMapper.selectPage(new Page<>(projectRequest.getPage(), projectRequest.getLimit()), projectRequest.simpleWrapper());
        if (iPage != null && CollectionUtil.isNotEmpty(iPage.getRecords())) {
            List<Long> dbIds = iPage.getRecords().stream().map(Project::getDatabaseId).collect(Collectors.toList());
            final List<Database> databaseList = databaseService.lambdaQuery().select(Database::getId, Database::getDbName).in(Database::getId, dbIds).list();
            Map<Long, Database> proDBMap = new HashMap<>();
            iPage.getRecords().forEach(v -> {
                if (CollectionUtil.isNotEmpty(databaseList)) {
                    List<Database> databases = databaseList.stream().filter(o -> o.getId().equals(v.getDatabaseId())).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(databases)) {
                        proDBMap.put(v.getId(), databases.get(0));
                    }
                }
            });
            IPage<ProjectListVo> voIPage = convert(iPage, ProjectListVo.class);
            for (ProjectListVo record : voIPage.getRecords()) {
                record.setDbName(proDBMap.get(record.getId()).getDbName());
                record.setDatabaseId(proDBMap.get(record.getId()).getId());
            }
            return voIPage;
        }
        return new Page<>();
    }

    @Override
    public boolean save(ProjectForm projectForm) {
        Project project = new Project();
        BeanUtils.copyProperties(projectForm, project);
        boolean save = this.saveOrUpdate(project);
        if (save && CollectionUtil.isNotEmpty(projectForm.getTemplates())) {
            //关联代码模板
            projectTemplateService.lambdaUpdate().eq(ProjectTemplate::getProjectId, projectForm.getId()).remove();
            List<ProTemplateDto> templates = projectForm.getTemplates();
            List<ProjectTemplate> projectTemplates = new ArrayList<>();
            templates.forEach(v -> {
                ProjectTemplate projectTemplate = new ProjectTemplate();
                projectTemplate.setProjectId(project.getId());
                projectTemplate.setTemplateId(v.getTemplateId());
                projectTemplate.setPackageName(v.getPackageName());
                projectTemplate.setOutputPath(v.getOutputPath());
                projectTemplates.add(projectTemplate);
            });
            projectTemplateService.saveOrUpdateBatch(projectTemplates);
        }
        return save;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public ProjectDetailVo voDetail(Long id) {
        Project project = lambdaQuery().getBaseMapper().selectById(id);
        if(project != null){
            //获取项目信息
            ProjectDetailVo projectDetailVo = new ProjectDetailVo();
            BeanUtils.copyProperties(project, projectDetailVo);
            //获取数据库
            Database database = databaseService.getBaseMapper().selectById(project.getDatabaseId());
            if(database != null){
                DatabaseVo databaseVo = new DatabaseVo();
                BeanUtils.copyProperties(database, databaseVo);
                projectDetailVo.setDatabase(databaseVo);
            }
            //获取模板信息
            List<ProjectTemplate> templates = projectTemplateService.lambdaQuery().eq(ProjectTemplate::getProjectId, project.getId()).list();
            if(CollectionUtil.isNotEmpty(templates)){
                List<ProTemplateDto> templateDtoList = new ArrayList<>();
                for (ProjectTemplate template : templates) {
                    ProTemplateDto proTemplateDto = new ProTemplateDto();
                    BeanUtils.copyProperties(template, proTemplateDto);
                    Template dbTemp = templateService.lambdaQuery().eq(Template::getId, template.getTemplateId()).one();
                    proTemplateDto.setTemplateName(dbTemp.getTemplateName());
                    proTemplateDto.setTemplateType(dbTemp.getTemplateType());
                    templateDtoList.add(proTemplateDto);
                }
                projectDetailVo.setTemplates(templateDtoList);
            }
            return projectDetailVo;
        }
        return null;
    }

    @Override
    public ProjectDetail detail(Long id) {
        Project project = lambdaQuery().getBaseMapper().selectById(id);
        if(project != null){
            //获取项目信息
            ProjectDetail projectDetail = new ProjectDetail();
            BeanUtils.copyProperties(project, projectDetail);
            //获取数据库
            projectDetail.setDatabase(databaseService.getBaseMapper().selectById(project.getDatabaseId()));
            //获取模板信息
            List<ProjectTemplate> projectTemplates = projectTemplateService.lambdaQuery().eq(ProjectTemplate::getProjectId, project.getId()).list();
            if(CollectionUtil.isNotEmpty(projectTemplates)){
                ArrayList<ProjectTemplateDto> projectTemplateDtoList = new ArrayList<>();
                for (ProjectTemplate projectTemplate : projectTemplates) {
                    ProjectTemplateDto projectTemplateDto = new ProjectTemplateDto();
                    BeanUtils.copyProperties(projectTemplate, projectTemplateDto);
                    projectTemplateDtoList.add(projectTemplateDto);
                }
                //查询模板类型&内容
                List<Template> templates = templateService.lambdaQuery().in(Template::getId, projectTemplates.stream().map(ProjectTemplate::getTemplateId).collect(Collectors.toList())).list();
                if(CollectionUtil.isNotEmpty(templates)){
                    for (ProjectTemplateDto projectTemplateDto : projectTemplateDtoList) {
                        for (Template template : templates) {
                            if(template.getId().equals(projectTemplateDto.getTemplateId())){
                                projectTemplateDto.setTemplateType(template.getTemplateType());
                                projectTemplateDto.setTemplateContent(template.getTemplateContent());
                                projectTemplateDto.setFilePrefix(template.getFilePrefix());
                                projectTemplateDto.setFileSuffix(template.getFileSuffix());
                            }
                        }
                    }
                }
                projectDetail.setProjectTemplates(projectTemplateDtoList);
            }
            return projectDetail;
        }
        return null;
    }

    @Override
    public IPage<ProjectListVo> option(ProjectRequest projectRequest) {
        return this.voPage(projectRequest);
    }
}
