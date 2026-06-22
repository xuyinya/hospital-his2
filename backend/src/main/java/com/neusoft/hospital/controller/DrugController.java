package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Drug;
import com.neusoft.hospital.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "药品管理")
@RestController
@RequestMapping("/api/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;

    @Operation(summary = "新增药品")
    @PostMapping
    public Result<Void> add(@RequestBody Drug drug) {
        drugService.add(drug);
        return Result.success();
    }

    @Operation(summary = "修改药品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Drug drug) {
        drug.setId(id);
        drugService.update(drug);
        return Result.success();
    }

    @Operation(summary = "删除药品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        drugService.delete(id);
        return Result.success();
    }

    @Operation(summary = "查询药品详情")
    @GetMapping("/{id}")
    public Result<Drug> getById(@PathVariable Long id) {
        return Result.success(drugService.getById(id));
    }

    @Operation(summary = "药品列表")
    @GetMapping("/list")
    public Result<PageResult<Drug>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String drugName) {
        return Result.success(drugService.list(drugName, page, size));
    }
}
