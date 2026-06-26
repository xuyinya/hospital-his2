package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Payment;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 收费数据访问层 —— 对应 payment 表
 * <p>提供缴费记录的增删改查操作，包括更新支付状态、分页查询缴费视图（含患者名称）等。</p>
 */
@Mapper
public interface PaymentMapper {

    /**
     * 新增缴费记录
     * <p>SQL：插入缴费信息（关联挂号/患者/费用类型/总金额/支付方式），初始状态为 0（待支付）</p>
     *
     * @param payment 缴费实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO payment(registration_id, patient_id, payment_type, total_amount, payment_method, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{paymentType}, #{totalAmount}, #{paymentMethod}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Payment payment);

    /**
     * 更新缴费状态
     * <p>SQL：按主键更新支付状态，并记录支付完成时间</p>
     *
     * @param id     缴费ID
     * @param status 状态（0=待支付，1=已支付，2=已退费）
     * @return 影响的行数
     */
    @Update("UPDATE payment SET status=#{status}, payment_time=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据ID查询缴费
     * <p>SQL：按主键 id 查询缴费记录</p>
     *
     * @param id 缴费ID
     * @return 缴费实体，未找到返回 null
     */
    @Select("SELECT * FROM payment WHERE id=#{id}")
    Payment selectById(Long id);

    /**
     * 分页查询缴费列表（含患者名称）
     * <p>XML 动态 SQL：按患者姓名和缴费状态分页查询，结果包含患者名称</p>
     *
     * @param patientName 患者姓名（可选，模糊匹配）
     * @param status      状态（可选，0=待支付，1=已支付，2=已退费）
     * @param offset      分页偏移量
     * @param pageSize    每页条数
     * @return 缴费分页列表（含患者名称）
     */
    // 带患者名称的分页列表（XML动态SQL）
    List<Payment> selectList(@Param("patientName") String patientName,
                                    @Param("status") Integer status,
                                    @Param("offset") int offset,
                                    @Param("pageSize") int pageSize);

    /**
     * 查询缴费总数（用于分页）
     * <p>XML 动态 SQL：按患者姓名和缴费状态统计记录数</p>
     *
     * @param patientName 患者姓名（可选）
     * @param status      状态（可选）
     * @return 符合条件的缴费总数
     */
    Long selectCount(@Param("patientName") String patientName,
                              @Param("status") Integer status);
}
