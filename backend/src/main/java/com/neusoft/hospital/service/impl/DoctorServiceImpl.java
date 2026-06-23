package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.vo.DoctorVO;
import com.neusoft.hospital.mapper.DoctorMapper;
import com.neusoft.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 医生管理服务实现类
 * 负责医生信息的查询、新增、修改和删除操作，提供分页查询和科室关联查询功能。
 * 删除医生时会校验是否存在关联的挂号记录，防止误删。
 */
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;

    /**
     * 获取所有医生列表（无分页）
     * 调用 Mapper 查询全部医生记录，返回无分页的完整列表。
     *
     * @return 医生实体列表
     */
    @Override
    public List<Doctor> list() {
        return doctorMapper.selectAll();
    }

    /**
     * 分页查询医生信息（含科室名称）
     * 根据医生姓名、科室ID进行模糊过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param doctorName 医生姓名（可选，模糊查询）
     * @param deptId     科室ID（可选）
     * @param page       当前页码（从1开始）
     * @param size       每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<DoctorVO> listVO(String doctorName, Long deptId, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行分页查询，返回带科室名称的VO列表
        List<DoctorVO> rows = doctorMapper.selectVOPage(doctorName, deptId, offset, size);
        // 查询满足条件的总记录数
        Long total = doctorMapper.selectVOCount(doctorName, deptId);
        return new PageResult<>(total, rows);
    }

    /**
     * 根据医生ID获取医生详情
     * 调用 Mapper 按主键查询单条医生记录。
     *
     * @param id 医生ID
     * @return 医生实体，若不存在则返回null
     */
    @Override
    public Doctor getById(Long id) {
        return doctorMapper.selectById(id);
    }

    /**
     * 根据科室ID获取该科室下的所有医生
     * 调用 Mapper 按科室ID查询关联的医生列表，用于前端下拉选择。
     *
     * @param deptId 科室ID
     * @return 该科室下的医生实体列表
     */
    @Override
    public List<Doctor> getByDeptId(Long deptId) {
        return doctorMapper.selectByDeptId(deptId);
    }

    /**
     * 根据科室ID获取医生列表（含科室名称）
     * 返回包含科室名称的医生VO列表，用于前端展示更多信息。
     *
     * @param deptId 科室ID
     * @return 该科室下的医生VO列表
     */
    @Override
    public List<DoctorVO> getByDeptIdVO(Long deptId) {
        return doctorMapper.selectByDeptIdVO(deptId);
    }

    /**
     * 新增医生
     * 如果医生状态未设置，则默认为1（正常/启用状态），然后插入数据库。
     *
     * @param doctor 医生实体
     */
    @Override
    public void add(Doctor doctor) {
        // 默认医生状态为启用（1），若未设置则赋予默认值
        if (doctor.getStatus() == null) doctor.setStatus(1);
        doctorMapper.insert(doctor);
    }

    /**
     * 修改医生信息
     * 根据医生ID更新数据库中对应的记录。
     *
     * @param doctor 待更新的医生实体（需包含ID）
     */
    @Override
    public void update(Doctor doctor) {
        doctorMapper.update(doctor);
    }

    /**
     * 删除医生
     * 先校验该医生是否有关联的挂号记录，若有则抛出异常阻止删除，
     * 确保数据的引用完整性。
     *
     * @param id 待删除的医生ID
     * @throws RuntimeException 该医生有挂号记录时抛出
     */
    @Override
    public void delete(Long id) {
        // 检查该医生是否存在关联的挂号记录
        long count = doctorMapper.countRegistrationsByDoctorId(id);
        if (count > 0) {
            throw new RuntimeException("该医生有 " + count + " 条挂号记录，不能删除");
        }
        doctorMapper.deleteById(id);
    }
}
