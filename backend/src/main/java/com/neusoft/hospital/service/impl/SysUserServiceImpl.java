package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.mapper.SysUserMapper;
import com.neusoft.hospital.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }
}
