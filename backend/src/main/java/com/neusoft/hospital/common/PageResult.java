package com.neusoft.hospital.common;

import lombok.Data;
import java.util.List;

/**
 * 通用分页结果封装类
 * <p>
 * 用于统一封装MyBatis分页查询的返回结果，包含总记录数和当前页数据列表。
 * 前端收到此结构后可直接用于分页表格的数据绑定。
 * </p>
 *
 * @param <T> 分页数据的类型
 */
@Data
public class PageResult<T> {
    /** 总记录数，用于前端分页组件计算总页数 */
    private Long total;
    /** 当前页的数据行列表 */
    private List<T> rows;

    /**
     * 构造分页结果
     *
     * @param total 总记录数
     * @param rows  当前页数据列表
     */
    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
