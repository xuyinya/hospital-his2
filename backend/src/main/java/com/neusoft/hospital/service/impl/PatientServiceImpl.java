package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.mapper.PatientMapper;
import com.neusoft.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 患者管理服务实现类
 * 提供患者信息的新增、修改、删除和查询功能，支持按姓名模糊查询的分页列表，
 * 以及通过身份证号精确查询患者（用于挂号时自动填充患者信息）。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientMapper patientMapper;

    /**
     * 新增患者
     * 调用 Mapper 将患者实体插入数据库。
     *
     * @param patient 患者实体
     */
    @Override
    public void add(Patient patient) {
        patientMapper.insert(patient);
    }

    /**
     * 修改患者信息
     * 根据患者ID更新数据库中对应的记录。
     *
     * @param patient 待更新的患者实体（需包含ID）
     */
    @Override
    public void update(Patient patient) {
        patientMapper.update(patient);
    }

    /**
     * 删除患者
     * 根据患者ID从数据库中删除对应的记录。
     *
     * @param id 待删除的患者ID
     */
    @Override
    public void delete(Long id) {
        patientMapper.deleteById(id);
    }

    @Override
    public void updatePassword(Long id, String encodedPassword) {
        patientMapper.updatePassword(id, encodedPassword);
    }

    /**
     * 根据患者ID获取患者详情
     * 调用 Mapper 按主键查询单条患者记录。
     *
     * @param id 患者ID
     * @return 患者实体，若不存在则返回null
     */
    @Override
    public Patient getById(Long id) {
        return patientMapper.selectById(id);
    }

    /**
     * 根据身份证号查询患者
     * 用于挂号时通过身份证号精确匹配，自动填充患者基本信息。
     *
     * @param idCard 身份证号
     * @return 患者实体，若不存在则返回null
     */
    @Override
    public Patient getByIdCard(String idCard) {
        return patientMapper.selectByIdCard(idCard);
    }

    /**
     * 分页查询患者列表
     * 根据患者姓名和医生ID进行过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param patientName 患者姓名（可选，模糊查询）
     * @param doctorId    医生ID（可选）
     * @param page        当前页码（从1开始）
     * @param size        每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<Patient> list(String patientName, Long doctorId, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行分页查询
        List<Patient> rows = patientMapper.selectList(patientName, doctorId, offset, size);
        // 查询满足条件的总记录数
        Long total = patientMapper.selectCount(patientName, doctorId);
        return new PageResult<>(total, rows);
    }
}
