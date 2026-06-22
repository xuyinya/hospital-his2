package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.entity.vo.PaymentVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment(registration_id, patient_id, payment_type, total_amount, payment_method, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{paymentType}, #{totalAmount}, #{paymentMethod}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Payment payment);

    @Update("UPDATE payment SET status=#{status}, payment_time=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT * FROM payment WHERE id=#{id}")
    Payment selectById(Long id);

    // 带患者名称的分页列表（XML动态SQL）
    List<PaymentVO> selectPaymentVO(@Param("patientName") String patientName,
                                    @Param("status") Integer status,
                                    @Param("offset") int offset,
                                    @Param("pageSize") int pageSize);

    Long selectPaymentVOCount(@Param("patientName") String patientName,
                              @Param("status") Integer status);

    // 不带患者名称的传统查询（保留兼容）
    List<Payment> selectList(@Param("patientId") Long patientId,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    Long selectCount(@Param("patientId") Long patientId);
}
