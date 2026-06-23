/**
 * 科室管理 API 模块
 *
 * 封装科室相关的接口：
 * - 分页查询科室列表（GET /api/department/list）
 * - 根据 ID 查询科室详情（GET /api/department/{id}）
 */

import request from '@/utils/request'

/**
 * 分页查询科室列表
 * @param {object} params 查询参数（page, pageSize, name 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getDepartmentList(params) {
  return request.get('/department/list', { params })
}

/**
 * 根据 ID 查询科室详情
 * @param {number|string} id 科室 ID
 * @returns {Promise} 科室详细信息
 */
export function getDepartmentById(id) {
  return request.get(`/department/${id}`)
}
