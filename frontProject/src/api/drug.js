/**
 * 药品管理 API 模块
 *
 * 封装药品信息相关的接口：
 * - 分页查询药品列表（GET /api/drug/list）
 * - 根据 ID 查询药品详情（GET /api/drug/{id}）
 * - 新增药品（POST /api/drug）
 * - 修改药品信息（PUT /api/drug/{id}）
 * - 删除药品（DELETE /api/drug/{id}）
 */

import request from '@/utils/request'

/**
 * 分页查询药品列表
 * @param {object} params 查询参数（page, pageSize, name, type 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getDrugList(params) {
  return request.get('/drug/list', { params })
}

/**
 * 根据 ID 查询药品详情
 * @param {number|string} id 药品 ID
 * @returns {Promise} 药品详细信息
 */
export function getDrugById(id) {
  return request.get(`/drug/${id}`)
}

/**
 * 新增药品
 * @param {object} data 药品信息（name, specification, unit, price 等）
 * @returns {Promise} 新增结果
 */
export function addDrug(data) {
  return request.post('/drug', data)
}

/**
 * 修改药品信息
 * @param {number|string} id 药品 ID
 * @param {object} data 更新的药品信息
 * @returns {Promise} 更新结果
 */
export function updateDrug(id, data) {
  return request.put(`/drug/${id}`, data)
}

/**
 * 删除药品
 * @param {number|string} id 药品 ID
 * @returns {Promise} 删除结果
 */
export function deleteDrug(id) {
  return request.delete(`/drug/${id}`)
}
