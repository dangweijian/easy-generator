package com.dwj.generator.common.annotation;

import com.dwj.generator.common.enums.OrderType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: dangweijian
 * @description: 排序注解
 * @create: 2020-01-16 13:24
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    OrderType order() default OrderType.DESC;
}
