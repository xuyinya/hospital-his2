package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Patient;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 患者数据访问层 —— 对应 patient 表
 * <p>提供患者信息的增删改查操作，包括按身份证号查询、分页查询、密码更新等。</p>
 */
@Mapper
public interface PatientMapper {

    /**
     * 新增患者记录
     * <p>SQL：插入患者基本信息和密码，自动记录创建时间和更新时间</p>
     *
     * @param patient 患者实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO patient(patient_name, gender, age, id_card, phone, address, password, create_time, update_time) " +
            "VALUES(#{patientName}, #{gender}, #{age}, #{idCard}, #{phone}, #{address}, #{password}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Patient patient);

    /**
     * 更新患者信息
     * <p>SQL：按主键更新患者的姓名、性别、年龄、身份证、电话和地址，自动更新修改时间</p>
     *
     * @param patient 患者实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE patient SET patient_name=#{patientName}, gender=#{gender}, age=#{age}, " +
            "id_card=#{idCard}, phone=#{phone}, address=#{address}, update_time=NOW() WHERE id=#{id}")
    int update(Patient patient);

    /**
     * 根据ID删除患者
     * <p>SQL：按主键物理删除患者记录</p>
     *
     * @param id 患者ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM patient WHERE id=#{id}")
    int deleteById(Long id);

    /**
     * 根据ID查询患者
     * <p>SQL：按主键 id 查询患者记录</p>
     *
     * @param id 患者ID
     * @return 患者实体，未找到返回 null
     */
    @Select("SELECT * FROM patient WHERE id=#{id}")
    Patient selectById(Long id);

    /**
     * 根据身份证号查询患者
     * <p>SQL：按 id_card 精确查询患者（身份证号唯一）</p>
     *
     * @param idCard 身份证号
     * @return 患者实体，未找到返回 null
     */
    @Select("SELECT * FROM patient WHERE id_card = #{idCard}")
    Patient selectByIdCard(@Param("idCard") String idCard);

    /**
     * 更新患者密码
     * <p>SQL：按主键更新患者登录密码</p>
     *
     * @param id       患者ID
     * @param password 加密后的新密码
     * @return 影响的行数
     */
    @Update("UPDATE patient SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 分页查询患者列表
     * <p>XML 动态 SQL：按患者姓名模糊查询和医生ID筛选，支持分页</p>
     *
     * @param patientName 患者姓名（可选，模糊匹配）
     * @param doctorId    医生ID（可选，用于关联查询出该医生接诊过的患者）
     * @param offset      分页偏移量
     * @param pageSize    每页条数
     * @return 患者分页列表
     */
    List<Patient> selectList(@Param("patientName") String patientName,
                             @Param("doctorId") Long doctorId,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    /**
     * 查询患者总数（用于分页）
     * <p>XML 动态 SQL：按患者姓名和医生ID统计符合条件的记录数</p>
     *
     * @param patientName 患者姓名（可选）
     * @param doctorId    医生ID（可选）
     * @return 符合条件的患者总数
     */
    Long selectCount(@Param("patientName") String patientName,
                     @Param("doctorId") Long doctorId);
}
