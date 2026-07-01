package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Drug;

/**
 * 药品管理 Service 接口
 * 提供药品的增删改查功能，支持按药品名称分页查询。
 */
public interface DrugService {

    /**
     * 新增药品
     *
     * @param drug 药品对象
     */
    void add(Drug drug);

    /**
     * 更新药品信息
     *
     * @param drug 药品对象
     */
    void update(Drug drug);

    /**
     * 删除药品
     *
     * @param id 药品ID
     */
    void delete(Long id);

    boolean decreaseStock(Long drugId, int quantity);

    /**
     * 根据药品ID获取药品详情
     *
     * @param id 药品ID
     * @return 药品对象
     */
    Drug getById(Long id);

    /**
     * 分页查询药品列表
     *
     * @param drugName 药品名称（可选，模糊查询）
     * @param page     当前页码
     * @param size     每页条数
     * @return 分页结果
     */
    PageResult<Drug> list(String drugName, Integer page, Integer size);
}
