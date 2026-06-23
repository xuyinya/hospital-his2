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
/**
 * 药品管理控制器
 * <p>
 * 提供药品信息的增删改查功能，支持按药品名称模糊搜索和分页查询。
 * 接口路径：/api/drug
 * </p>
 */
public class DrugController {

    private final DrugService drugService;

    /**
     * 新增药品
     * <p>
     * 添加一条新的药品记录到系统中，包括药品名称、规格、单价、库存等信息。
     * </p>
     *
     * @param drug 药品信息
     * @return 操作结果
     */
    @Operation(summary = "新增药品")
    @PostMapping
    public Result<Void> add(@RequestBody Drug drug) {
        drugService.add(drug);
        return Result.success();
    }

    /**
     * 修改药品信息
     * <p>
     * 根据药品ID更新指定药品的信息，如名称、规格、单价、库存等。
     * </p>
     *
     * @param id   药品ID
     * @param drug 更新后的药品信息
     * @return 操作结果
     */
    @Operation(summary = "修改药品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Drug drug) {
        drug.setId(id);
        drugService.update(drug);
        return Result.success();
    }

    /**
     * 删除药品
     * <p>
     * 根据药品ID删除指定药品记录。
     * </p>
     *
     * @param id 药品ID
     * @return 操作结果
     */
    @Operation(summary = "删除药品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        drugService.delete(id);
        return Result.success();
    }

    /**
     * 查询药品详情
     * <p>
     * 根据药品ID查询单个药品的详细信息，包括名称、规格、单价、库存等。
     * </p>
     *
     * @param id 药品ID
     * @return 指定药品的详细信息
     */
    @Operation(summary = "查询药品详情")
    @GetMapping("/{id}")
    public Result<Drug> getById(@PathVariable Long id) {
        return Result.success(drugService.getById(id));
    }

    /**
     * 分页查询药品列表
     * <p>
     * 根据药品名称进行模糊分页查询，返回药品列表。
     * 支持按药品名称模糊搜索，无搜索条件时返回全部药品。
     * </p>
     *
     * @param page     页码，默认值为1
     * @param size     每页记录数，默认值为10
     * @param drugName 药品名称（可选），用于模糊搜索
     * @return 分页结果，包含药品列表和总记录数
     */
    @Operation(summary = "药品列表")
    @GetMapping("/list")
    public Result<PageResult<Drug>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String drugName) {
        return Result.success(drugService.list(drugName, page, size));
    }
}
