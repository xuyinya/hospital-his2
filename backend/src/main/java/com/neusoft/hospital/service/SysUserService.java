package com.neusoft.hospital.service;

import com.neusoft.hospital.entity.SysUser;

/**
 * 系统用户 Service 接口
 * 提供系统用户（管理员/医生账号）的基础查询功能，用于登录认证场景。
 */
public interface SysUserService {

    /**
     * 根据用户名获取系统用户
     *
     * @param username 用户名
     * @return 系统用户对象
     */
    SysUser getByUsername(String username);

    /**
     * 根据用户ID获取系统用户
     *
     * @param id 用户ID
     * @return 系统用户对象
     */
    SysUser getById(Long id);
}
