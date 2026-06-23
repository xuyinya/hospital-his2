package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.vo.PrescriptionVO;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 处方数据访问层 —— 对应 prescription 表
 * <p>提供处方记录的增删改查操作，包括更新总金额、更新状态、分页查询处方视图等。</p>
 */
@Mapper
public interface PrescriptionMapper {

    /**
     * 新增处方记录
     * <p>SQL：插入处方信息（关联挂号/患者/医生），初始总金额为0，状态为 0（未取药）</p>
     *
     * @param prescription 处方实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO prescription(registration_id, patient_id, doctor_id, total_amount, status, create_time) " +
            "VALUES(#{registrationId}, #{patientId}, #{doctorId}, 0, 0, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Prescription prescription);

    /**
     * 更新处方总金额
     * <p>SQL：按主键更新处方的 total_amount（由明细合计计算后回写）</p>
     *
     * @param id          处方ID
     * @param totalAmount 总金额
     * @return 影响的行数
     */
    @Update("UPDATE prescription SET total_amount=#{totalAmount} WHERE id=#{id}")
    int updateTotalAmount(@Param("id") Long id, @Param("totalAmount") BigDecimal totalAmount);

    /**
     * 更新处方状态
     * <p>SQL：按主键更新处方状态（0=未取药，1=已取药）</p>
     *
     * @param id     处方ID
     * @param status 状态值
     * @return 影响的行数
     */
    @Update("UPDATE prescription SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据ID查询处方
     * <p>SQL：按主键 id 查询处方记录</p>
     *
     * @param id 处方ID
     * @return 处方实体，未找到返回 null
     */
    @Select("SELECT * FROM prescription WHERE id=#{id}")
    Prescription selectById(Long id);

    /**
     * 分页查询处方 VO 列表（含患者/医生信息）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态等多条件分页查询处方视图</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选，0=未取药，1=已取药）
     * @param offset         分页偏移量
     * @param pageSize       每页条数
     * @return 处方 VO 分页列表
     */
    List<PrescriptionVO> selectPrescriptionVO(@Param("registrationId") Long registrationId,
                                               @Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("status") Integer status,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    /**
     * 查询处方 VO 总数（用于分页）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态统计记录数</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选）
     * @return 符合条件的处方总数
     */
    Long selectPrescriptionVOCount(@Param("registrationId") Long registrationId,
                                    @Param("patientId") Long patientId,
                                    @Param("doctorId") Long doctorId,
                                    @Param("status") Integer status);
}
