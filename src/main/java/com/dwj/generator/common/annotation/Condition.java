package com.dwj.generator.common.annotation;

import com.dwj.generator.common.enums.ConditionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: dangweijian
 * @description: 条件注解
 * @create: 2020-01-15 17:24
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {
    ConditionType type() default ConditionType.EQ;
}
