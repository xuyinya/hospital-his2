package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.mapper.SysUserMapper;
import com.neusoft.hospital.service.SysUserService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 系统用户管理服务实现类
 * 提供系统用户（管理员/操作员）的查询功能，支持按用户名和用户ID查询。
 * 用于登录认证模块校验用户身份。
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    /**
     * 根据用户名查询系统用户
     * 用于登录认证时根据输入的用户名获取用户信息，包含密码哈希值以进行密码校验。
     *
     * @param username 用户名
     * @return 系统用户实体，若不存在则返回null
     */
    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    /**
     * 根据用户ID查询系统用户
     * 调用 Mapper 按主键查询单条系统用户记录。
     *
     * @param id 用户ID
     * @return 系统用户实体，若不存在则返回null
     */
    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public void updatePassword(Long id, String encodedPassword) {
        sysUserMapper.updatePassword(id, encodedPassword);
    }

    @Override
    public List<SysUser> listAll() {
        return sysUserMapper.selectAll();
    }

    @Override
    public void add(SysUser user) {
        sysUserMapper.insert(user);
    }

    @Override
    public void delete(Long id) {
        sysUserMapper.deleteById(id);
    }
}
