package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Drug;
import com.neusoft.hospital.mapper.DrugMapper;
import com.neusoft.hospital.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 药品管理服务实现类
 * 提供药品信息的新增、修改、删除、查询功能，支持按药品名称模糊查询的分页列表。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final DrugMapper drugMapper;

    /**
     * 新增药品
     * 调用 Mapper 将药品实体插入数据库。
     *
     * @param drug 药品实体
     */
    @Override
    public void add(Drug drug) {
        drugMapper.insert(drug);
    }

    /**
     * 修改药品信息
     * 根据药品ID更新数据库中对应的记录。
     *
     * @param drug 待更新的药品实体（需包含ID）
     */
    @Override
    public void update(Drug drug) {
        drugMapper.update(drug);
    }

    /**
     * 删除药品
     * 根据药品ID从数据库中删除对应的记录。
     *
     * @param id 待删除的药品ID
     */
    @Override
    public void delete(Long id) {
        drugMapper.deleteById(id);
    }

    /**
     * 根据药品ID获取药品详情
     * 调用 Mapper 按主键查询单条药品记录。
     *
     * @param id 药品ID
     * @return 药品实体，若不存在则返回null
     */
    @Override
    public Drug getById(Long id) {
        return drugMapper.selectById(id);
    }

    /**
     * 分页查询药品列表
     * 根据药品名称进行模糊过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param drugName 药品名称（可选，模糊查询）
     * @param page     当前页码（从1开始）
     * @param size     每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<Drug> list(String drugName, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行分页查询
        List<Drug> rows = drugMapper.selectList(drugName, offset, size);
        // 查询满足条件的总记录数
        Long total = drugMapper.selectCount(drugName);
        return new PageResult<>(total, rows);
    }
}
