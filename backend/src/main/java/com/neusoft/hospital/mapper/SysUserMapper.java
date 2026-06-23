package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.SysUser;
import org.apache.ibatis.annotations.*;

/**
 * 系统用户数据访问层 —— 对应 sys_user 表
 * <p>提供系统用户的查询和新增操作，包括按用户名查询（用于登录认证）、按ID查询等。</p>
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名查询系统用户
     * <p>SQL：按 username 精确查询用户记录（用于登录认证）</p>
     *
     * @param username 用户名
     * @return 系统用户实体，未找到返回 null
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询系统用户
     * <p>SQL：按主键 id 查询用户记录</p>
     *
     * @param id 用户ID
     * @return 系统用户实体，未找到返回 null
     */
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Long id);

    /**
     * 新增系统用户
     * <p>SQL：插入用户信息（用户名、密码、姓名、角色、关联医生ID、状态），自动记录创建和更新时间</p>
     *
     * @param sysUser 系统用户实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO sys_user(username, password, real_name, role, doctor_id, status, create_time, update_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{role}, #{doctorId}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser sysUser);
}
