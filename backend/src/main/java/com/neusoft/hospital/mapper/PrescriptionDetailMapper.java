package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.entity.vo.PrescriptionDetailVO;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PrescriptionDetailMapper {

    @Insert("INSERT INTO prescription_detail(prescription_id, drug_id, quantity, unit_price, amount, usage_method) " +
            "VALUES(#{prescriptionId}, #{drugId}, #{quantity}, #{unitPrice}, #{amount}, #{usageMethod})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PrescriptionDetail detail);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM prescription_detail WHERE prescription_id=#{prescriptionId}")
    BigDecimal sumAmountByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    List<PrescriptionDetailVO> selectDetailVO(@Param("prescriptionId") Long prescriptionId);
}
