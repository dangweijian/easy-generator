package com.dwj.generator.service.impl;

import com.dwj.generator.config.mybatis.plus.CommonServiceImpl;
import com.dwj.generator.dao.entity.Admin;
import com.dwj.generator.dao.mapper.AdminMapper;
import com.dwj.generator.service.IAdminService;
import org.springframework.stereotype.Service;

/**
 * 管理员表 服务实现类
 *
 * @author easy-generator
 * @since 2021-04-08
 */
@Service
public class AdminServiceImpl extends CommonServiceImpl<AdminMapper, Admin> implements IAdminService {

}
