package com.dwj.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwj.generator.dao.entity.Template;
import com.dwj.generator.model.template.TemplateForm;
import com.dwj.generator.model.template.TemplateRequest;
import com.dwj.generator.model.template.TemplateVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dwjian
 * @since 2021-03-26
 */
public interface ITemplateService extends IService<Template> {

    IPage<TemplateVo> list(TemplateRequest templateRequest);

    boolean save(TemplateForm templateForm);

    Template detail(Long id);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);
}
