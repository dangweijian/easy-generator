package com.dwj.generator.config.mybatis.plus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Objects;


@Slf4j
public class CommonServiceImpl<M extends CommonMapper<T>, T> extends ServiceImpl<M, T> {

    public <E> IPage<E> voPage(QueryCriteria<T,E> criteria) {
        IPage<T> tPage = baseMapper.voPage(new Page<>(criteria.getPage(), criteria.getLimit()), criteria.simpleWrapper());
        if(criteria.getVoClass() != null){
            return convert(tPage, criteria.getVoClass());
        }
        return new Page<>();
    }

    public <E> IPage<E> convert(IPage<T> page, Class<E> clazz){
        if(clazz != null){
            IPage<E> voPage = page.convert(t -> {
                E instance = null;
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("vo转换异常", e);
                }
                if (Objects.nonNull(instance)) {
                    BeanUtils.copyProperties(t, instance);
                }
                return instance;
            });
            if(voPage != null){
                return voPage;
            }
        }
        return new Page<>();
    }
}
