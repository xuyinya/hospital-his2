package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Drug;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 药品数据访问层 —— 对应 drug 表
 * <p>提供药品的增删改查操作，包括分页查询、逻辑删除等。</p>
 */
@Mapper
public interface DrugMapper {

    /**
     * 新增药品记录
     * <p>SQL：插入药品名称、编码、规格、单位、厂商、单价、库存等字段，默认状态为启用(1)</p>
     *
     * @param drug 药品实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO drug(drug_name, drug_code, specification, unit, manufacturer, unit_price, stock, status) " +
            "VALUES(#{drugName}, #{drugCode}, #{specification}, #{unit}, #{manufacturer}, #{unitPrice}, #{stock}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);

    /**
     * 更新药品信息
     * <p>SQL：按主键更新药品的名称、编码、规格、单位、厂商、单价和库存</p>
     *
     * @param drug 药品实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE drug SET drug_name=#{drugName}, drug_code=#{drugCode}, specification=#{specification}, " +
            "unit=#{unit}, manufacturer=#{manufacturer}, unit_price=#{unitPrice}, stock=#{stock}, status=#{status} WHERE id=#{id}")
    int update(Drug drug);

    /**
     * 逻辑删除药品（置状态为无效）
     * <p>SQL：将指定药品的 status 置为 0（禁用），不执行物理删除</p>
     *
     * @param id 药品ID
     * @return 影响的行数
     */
    @Update("UPDATE drug SET status=0 WHERE id=#{id}")
    int deleteById(Long id);

    /**
     * 根据ID查询药品
     * <p>SQL：按主键 id 查询药品记录</p>
     *
     * @param id 药品ID
     * @return 药品实体，未找到返回 null
     */
    @Select("SELECT * FROM drug WHERE id=#{id}")
    Drug selectById(Long id);

    /**
     * 分页查询药品列表
     * <p>XML 动态 SQL：按药品名称模糊查询，支持分页</p>
     *
     * @param drugName 药品名称（可选，模糊匹配）
     * @param offset   分页偏移量
     * @param pageSize 每页条数
     * @return 药品分页列表
     */
    // 动态SQL放XML里
    List<Drug> selectList(@Param("drugName") String drugName,
                          @Param("offset") int offset,
                          @Param("pageSize") int pageSize);

    /**
     * 查询药品总数（用于分页）
     * <p>XML 动态 SQL：按药品名称统计符合条件的记录数</p>
     *
     * @param drugName 药品名称（可选）
     * @return 符合条件的药品总数
     */
    Long selectCount(@Param("drugName") String drugName);
}
