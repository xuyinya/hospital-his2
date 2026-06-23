package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;
import com.neusoft.hospital.mapper.MedicalRecordMapper;
import com.neusoft.hospital.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 病历管理服务实现类
 * 负责病历的新增、修改和查询操作，支持按挂号关联查询和分页列表查询。
 * 一条挂号记录对应一份病历，通过挂号ID可以唯一关联到病历。
 */
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordMapper medicalRecordMapper;

    /**
     * 新增病历
     * 调用 Mapper 将病历实体插入数据库。
     *
     * @param medicalRecord 病历实体
     */
    @Override
    public void add(MedicalRecord medicalRecord) {
        medicalRecordMapper.insert(medicalRecord);
    }

    /**
     * 修改病历信息
     * 根据病历ID更新数据库中对应的记录。
     *
     * @param medicalRecord 待更新的病历实体（需包含ID）
     */
    @Override
    public void update(MedicalRecord medicalRecord) {
        medicalRecordMapper.update(medicalRecord);
    }

    /**
     * 根据病历ID获取病历详情
     * 调用 Mapper 按主键查询单条病历记录。
     *
     * @param id 病历ID
     * @return 病历实体，若不存在则返回null
     */
    @Override
    public MedicalRecord getById(Long id) {
        return medicalRecordMapper.selectById(id);
    }

    /**
     * 根据挂号ID获取关联的病历
     * 通过挂号ID查询对应的病历记录，一条挂号记录唯一对应一份病历。
     *
     * @param registrationId 挂号ID
     * @return 病历实体，若不存在则返回null
     */
    @Override
    public MedicalRecord getByRegistrationId(Long registrationId) {
        return medicalRecordMapper.selectByRegistrationId(registrationId);
    }

    /**
     * 分页查询病历列表（含关联信息）
     * 根据挂号ID、患者ID、医生ID和患者姓名进行多条件过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param patientName    患者姓名（可选，模糊查询）
     * @param page           当前页码（从1开始）
     * @param size           每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<MedicalRecordVO> list(Long registrationId, Long patientId, Long doctorId, String patientName, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带患者和医生等关联信息的VO列表
        List<MedicalRecordVO> rows = medicalRecordMapper.selectMedicalRecordVO(registrationId, patientId, doctorId, patientName, offset, size);
        // 查询满足条件的总记录数
        Long total = medicalRecordMapper.selectMedicalRecordVOCount(registrationId, patientId, doctorId, patientName);
        return new PageResult<>(total, rows);
    }
}
