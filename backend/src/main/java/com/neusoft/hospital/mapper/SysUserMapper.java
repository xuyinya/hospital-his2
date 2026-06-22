package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.SysUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SysUserMapper {

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Long id);

    @Insert("INSERT INTO sys_user(username, password, real_name, role, doctor_id, status, create_time, update_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{role}, #{doctorId}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser sysUser);
}
