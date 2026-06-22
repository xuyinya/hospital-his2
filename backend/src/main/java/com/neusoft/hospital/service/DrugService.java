package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Drug;

public interface DrugService {
    void add(Drug drug);
    void update(Drug drug);
    void delete(Long id);
    Drug getById(Long id);
    PageResult<Drug> list(String drugName, Integer page, Integer size);
}
