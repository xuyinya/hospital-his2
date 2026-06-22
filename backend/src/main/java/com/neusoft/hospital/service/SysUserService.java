package com.neusoft.hospital.service;

import com.neusoft.hospital.entity.SysUser;

public interface SysUserService {
    SysUser getByUsername(String username);
    SysUser getById(Long id);
}
