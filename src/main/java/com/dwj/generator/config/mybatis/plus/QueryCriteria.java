package com.dwj.generator.config.mybatis.plus;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dwj.generator.common.annotation.Condition;
import com.dwj.generator.common.annotation.Order;
import com.dwj.generator.common.utils.ConvertUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: dangweijian
 * @description:
 * @create: 2020-09-14 15:47
 **/
@Data
@Slf4j
public class QueryCriteria<T,E> {

    private long page = 1;
    private long limit = 10;
    private boolean isDel;
    private boolean isInjectRole = true;
    private boolean isInjectClass = true;
    private List<String> userRoleIds;
    private List<String> userClassIds;

    public QueryWrapper<T> simpleWrapper(){
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Condition condition = field.getAnnotation(Condition.class);
            if(condition != null){
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(this);
                } catch (IllegalAccessException e) {
                    log.warn("reflection anomaly!");
                }
                if(!StrUtil.isEmptyIfStr(value)){
                    switch (condition.type()){
                        case EQ:
                            wrapper.eq(ConvertUtil.humpToLine2(field.getName()), value);
                            break;
                        case LIKE:
                            wrapper.like(ConvertUtil.humpToLine2(field.getName()), value);
                    }
                }
            }
            Order order = field.getAnnotation(Order.class);
            if(order != null){
                if(!StrUtil.isEmptyIfStr(order.order())){
                    switch (order.order()){
                        case DESC:
                            wrapper.orderByDesc(ConvertUtil.humpToLine2(field.getName()));
                            break;
                        case ASC:
                            wrapper.orderByAsc(ConvertUtil.humpToLine2(field.getName()));
                    }
                }
            }
        }
        return wrapper;
    }

    protected  Class<E> getVoClass(){
        return null;
    }
}
