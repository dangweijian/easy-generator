package com.dwj.generator.config.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.dwj.generator.common.utils.AesUtil;
import com.dwj.generator.common.utils.SessionUtil;
import com.dwj.generator.config.admin.AdminContextHolder;
import com.dwj.generator.controller.AdminController;
import com.dwj.generator.dao.entity.Admin;
import com.dwj.generator.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AccessInterceptor
 * @Description 访问拦截器
 * @Author dwjian
 * @Date 2021/4/8 10:49
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix="spring.interceptor",name = "loginInterceptor", havingValue = "true")
public class LoginInterceptor extends AbstractInterceptor implements HandlerInterceptor {

    private static Map<Long, Admin> adminCache = new HashMap<>();

    @Resource
    private IAdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //校验登录
        Object loginToken = SessionUtil.getSessionAttribute(AdminController.LOGIN_KEY);
        if(loginToken == null){
            response.sendRedirect(request.getContextPath() + "/page/login.html");
            return false;
        }
        //校验token
        Admin admin = JSONUtil.toBean(AesUtil.decrypt(AdminController.ENCRYPT_KEY, loginToken.toString()), Admin.class);
        //缓存获取
        if(adminCache.containsKey(admin.getId())){
            Admin cacheAdmin = adminCache.get(admin.getId());
            if(!admin.getUsername().equals(cacheAdmin.getUsername())
                    || !admin.getPassword().equals(cacheAdmin.getPassword())
                    || !DateUtil.format(admin.getLastLoginDate(), "yyyy-MM-dd HH:mm:ss").equals(DateUtil.format(cacheAdmin.getLastLoginDate(), "yyyy-MM-dd HH:mm:ss"))){
                Admin targetAdmin = adminService.lambdaQuery()
                        .eq(Admin::getUsername, admin.getUsername())
                        .eq(Admin::getPassword, admin.getPassword())
                        .eq(Admin::getLastLoginDate, DateUtil.format(admin.getLastLoginDate(), "yyyy-MM-dd HH:mm:ss")).one();
                if(targetAdmin == null){
                    adminCache.remove(admin.getId());
                    response.sendRedirect(request.getContextPath() + "/page/login.html");
                    return false;
                }else {
                    adminCache.put(targetAdmin.getId(), targetAdmin);
                }
            }
        }else {
            Admin targetAdmin = adminService.lambdaQuery()
                    .eq(Admin::getUsername, admin.getUsername())
                    .eq(Admin::getPassword, admin.getPassword())
                    .eq(Admin::getLastLoginDate, DateUtil.format(admin.getLastLoginDate(), "yyyy-MM-dd HH:mm:ss")).one();
            if(targetAdmin == null){
                response.sendRedirect(request.getContextPath() + "/page/login.html");
                return false;
            }
            adminCache.put(targetAdmin.getId(), targetAdmin);
        }
        AdminContextHolder.setAdminContext(adminCache.get(admin.getId()));
        return true;
    }

    @Override
    public String pathPatterns() {
        return "/**";
    }

    @Override
    public Set<String> excludePathPatterns() {
        HashSet<String> pathMap = new HashSet<>();
        pathMap.add("/page/login.html");
        pathMap.add("/api/init.json");
        pathMap.add("/css/**");
        pathMap.add("/images/**");
        pathMap.add("/js/**");
        pathMap.add("/lib/**");
        pathMap.add("/api/admin/login");
        return pathMap;
    }

    @Override
    public int order() {
        return 0;
    }
}
