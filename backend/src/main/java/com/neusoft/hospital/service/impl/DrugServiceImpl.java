package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Drug;
import com.neusoft.hospital.mapper.DrugMapper;
import com.neusoft.hospital.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final DrugMapper drugMapper;

    @Override
    public void add(Drug drug) {
        drugMapper.insert(drug);
    }

    @Override
    public void update(Drug drug) {
        drugMapper.update(drug);
    }

    @Override
    public void delete(Long id) {
        drugMapper.deleteById(id);
    }

    @Override
    public Drug getById(Long id) {
        return drugMapper.selectById(id);
    }

    @Override
    public PageResult<Drug> list(String drugName, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Drug> rows = drugMapper.selectList(drugName, offset, size);
        Long total = drugMapper.selectCount(drugName);
        return new PageResult<>(total, rows);
    }
}
