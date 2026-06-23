package com.neusoft.hospital.common;

import lombok.Data;

/**
 * 统一API响应结果封装类
 * <p>
 * 所有Controller接口均使用此类作为统一返回格式，保证前后端交互的数据结构一致性。
 * 约定：code为200表示成功，500表示失败。
 * </p>
 *
 * @param <T> 响应数据体的类型
 */
@Data
public class Result<T> {
    /** 状态码：200=成功，500=失败 */
    private Integer code;
    /** 提示信息 */
    private String message;
    /** 响应数据体 */
    private T data;

    /**
     * 返回成功响应（无数据体）
     *
     * @param <T> 数据类型
     * @return 成功结果，data为null
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 返回成功响应（带数据体）
     *
     * @param data 要返回的数据
     * @param <T>  数据类型
     * @return 成功结果，包含数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);          // 设置成功状态码
        result.setMessage("操作成功"); // 默认成功提示
        result.setData(data);         // 设置响应数据
        return result;
    }

    /**
     * 返回失败响应
     *
     * @param message 错误描述信息
     * @param <T>     数据类型
     * @return 失败结果，不含数据体
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);          // 设置失败状态码
        result.setMessage(message);   // 设置错误描述
        return result;
    }
}
