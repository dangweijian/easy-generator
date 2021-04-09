package com.dwj.generator.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.common.utils.AesUtil;
import com.dwj.generator.common.utils.SessionUtil;
import com.dwj.generator.config.admin.AdminContextHolder;
import com.dwj.generator.dao.entity.Admin;
import com.dwj.generator.model.admin.LoginForm;
import com.dwj.generator.model.admin.LoginVo;
import com.dwj.generator.model.admin.UpdateForm;
import com.dwj.generator.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 管理员 前端控制器
 *
 * @author easy-generator
 * @since 2021-04-08
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    public final static String LOGIN_KEY = "login-token";
    public final static String ENCRYPT_KEY = "easy-generator-encrypt-key";
    public final static Integer EXPIRED_TIME = 30*60;

    @Resource
    private IAdminService adminService;

    @PostMapping("/login")
    public Response<LoginVo> login(@RequestBody LoginForm form){
        if(form.getUsername() == null || form.getPassword() == null){
            return Response.fail("登录失败！");
        }
        Admin admin = adminService.lambdaQuery().eq(Admin::getUsername, form.getUsername()).eq(Admin::getPassword, form.getPassword()).one();
        if(admin == null){
            return Response.fail("账号或密码错误！");
        }
        Date lastLoginDate = DateUtil.date();
        boolean update = adminService.lambdaUpdate().set(Admin::getLastLoginDate, DateUtil.format(lastLoginDate, "yyyy-MM-dd HH:mm:ss")).eq(Admin::getId, admin.getId()).update();
        if(update){
            admin.setLastLoginDate(lastLoginDate);
            try {
                String encrypt = AesUtil.encrypt(ENCRYPT_KEY, JSONUtil.toJsonStr(admin));
                SessionUtil.setSessionAttribute(LOGIN_KEY, encrypt, EXPIRED_TIME);
                return Response.success(new LoginVo(admin.getUsername(), encrypt));
            } catch (Exception e) {
                log.error("登录异常", e);
                return Response.fail("登录异常！");
            }
        }
        return Response.fail("登录失败！");
    }

    @PostMapping("/logout")
    public Response<LoginVo> logout(){
        SessionUtil.getSession().removeAttribute(LOGIN_KEY);
        return Response.success("退出成功！");
    }

    @PostMapping("/update")
    public Response<LoginVo> update(@RequestBody UpdateForm form){
        Admin currentAdmin = AdminContextHolder.getAdminContext();
        if(currentAdmin == null){
            return Response.fail("修改失败！");
        }
        if(!form.getNewPassword().equals(form.getAgainPassword())){
            return Response.fail("两次密码不一致！");
        }
        if(!form.getOldPassword().equals(currentAdmin.getPassword())){
            return Response.fail("旧密码错误！");
        }
        boolean update = adminService.lambdaUpdate().eq(Admin::getId, currentAdmin.getId()).set(Admin::getPassword, form.getNewPassword()).update();
        if(update){
            SessionUtil.getSession().removeAttribute(LOGIN_KEY);
            return Response.success("修改成功！");
        }
        return Response.fail("修改失败！");
    }
}
