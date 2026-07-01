package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.PrescriptionDetail;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 处方明细数据访问层 —— 对应 prescription_detail 表
 * <p>提供处方明细的增删改查操作，包括统计处方总金额、查询明细视图（含药品信息）等。</p>
 */
@Mapper
public interface PrescriptionDetailMapper {

    /**
     * 新增处方明细记录
     * <p>SQL：插入处方明细（关联处方/药品/数量/单价/金额/用法用量）</p>
     *
     * @param detail 处方明细实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO prescription_detail(prescription_id, drug_id, quantity, unit_price, amount, usage_method) " +
            "VALUES(#{prescriptionId}, #{drugId}, #{quantity}, #{unitPrice}, #{amount}, #{usageMethod})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PrescriptionDetail detail);

    @Delete("DELETE FROM prescription_detail WHERE prescription_id=#{prescriptionId}")
    int deleteByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    /**
     * 统计指定处方的总金额
     * <p>SQL：对处方明细的 amount 字段求和，无记录时返回 0</p>
     *
     * @param prescriptionId 处方ID
     * @return 处方总金额
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM prescription_detail WHERE prescription_id=#{prescriptionId}")
    BigDecimal sumAmountByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    /**
     * 查询处方明细列表（含药品名称/规格等信息）
     * <p>XML 动态 SQL：连表查询 prescription_detail + drug，返回含药品信息的明细视图</p>
     *
     * @param prescriptionId 处方ID
     * @return 处方明细列表（含药品信息）
     */
    List<PrescriptionDetail> selectByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}
