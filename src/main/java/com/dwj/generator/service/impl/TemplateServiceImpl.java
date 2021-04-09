package com.dwj.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dwj.generator.config.mybatis.plus.CommonServiceImpl;
import com.dwj.generator.dao.entity.Template;
import com.dwj.generator.dao.mapper.TemplateMapper;
import com.dwj.generator.model.template.TemplateForm;
import com.dwj.generator.model.template.TemplateRequest;
import com.dwj.generator.model.template.TemplateVo;
import com.dwj.generator.service.ITemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dwjian
 * @since 2021-03-26
 */
@Service
public class TemplateServiceImpl extends CommonServiceImpl<TemplateMapper, Template> implements ITemplateService {

    @Override
    public IPage<TemplateVo> list(TemplateRequest templateRequest) {
        return this.voPage(templateRequest);
    }

    @Override
    public boolean save(TemplateForm templateForm) {
        Template template = new Template();
        BeanUtils.copyProperties(templateForm, template);
        return this.saveOrUpdate(template);
    }

    @Override
    public Template detail(Long id) {
        return this.lambdaQuery().eq(Template::getId, id).one();
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }
}
