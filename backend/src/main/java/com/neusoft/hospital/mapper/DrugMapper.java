package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Drug;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DrugMapper {

    @Insert("INSERT INTO drug(drug_name, drug_code, specification, unit, manufacturer, unit_price, stock, status) " +
            "VALUES(#{drugName}, #{drugCode}, #{specification}, #{unit}, #{manufacturer}, #{unitPrice}, #{stock}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);

    @Update("UPDATE drug SET drug_name=#{drugName}, drug_code=#{drugCode}, specification=#{specification}, " +
            "unit=#{unit}, manufacturer=#{manufacturer}, unit_price=#{unitPrice}, stock=#{stock} WHERE id=#{id}")
    int update(Drug drug);

    @Update("UPDATE drug SET status=0 WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT * FROM drug WHERE id=#{id}")
    Drug selectById(Long id);

    // 动态SQL放XML里
    List<Drug> selectList(@Param("drugName") String drugName,
                          @Param("offset") int offset,
                          @Param("pageSize") int pageSize);

    Long selectCount(@Param("drugName") String drugName);
}
