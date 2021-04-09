package com.dwj.generator.config.web;

import com.dwj.generator.common.utils.SpringUtil;
import com.dwj.generator.config.interceptor.AbstractInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, AbstractInterceptor> interceptorMap = SpringUtil.getBeansOfType(AbstractInterceptor.class);
        if(interceptorMap.size() > 0){
            List<AbstractInterceptor> interceptorList = interceptorMap.values().stream().sorted(Comparator.comparing(AbstractInterceptor::order))
                    .collect(Collectors.toList());
            for (AbstractInterceptor interceptor : interceptorList) {
                InterceptorRegistration interceptorRegistration = registry.addInterceptor((HandlerInterceptor) interceptor)
                        .addPathPatterns(interceptor.pathPatterns());
                for (String excludePattern : interceptor.excludePathPatterns()) {
                    interceptorRegistration.excludePathPatterns(excludePattern);
                }
            }
        }
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/","/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}