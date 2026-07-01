/**
 * 收费管理 API 模块
 *
 * 封装收费相关的接口：
 * - 分页查询收费记录（GET /api/payment/list）
 * - 根据 ID 查询收费详情（GET /api/payment/{id}）
 * - 新增收费记录（POST /api/payment）
 * - 更新支付状态（PUT /api/payment/{id}/status）
 */

import request from '@/utils/request'

/**
 * 分页查询收费记录列表
 * @param {object} params 查询参数（page, pageSize, patientId, status 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getPaymentList(params) {
  return request.get('/payment/list', { params })
}

/**
 * 根据 ID 查询收费详情
 * @param {number|string} id 收费记录 ID
 * @returns {Promise} 收费详细信息
 */
export function getPaymentById(id) {
  return request.get(`/payment/${id}`)
}

/**
 * 新增收费记录
 * @param {object} data 收费信息（regId, patientId, items, totalFee 等）
 * @returns {Promise} 新增结果
 */
export function addPayment(data) {
  return request.post('/payment', data)
}

/**
 * 更新支付状态（确认支付 / 取消支付）
 * @param {number|string} id 收费记录 ID
 * @param {number|string} status 支付状态（如 1=已支付, 0=未支付）
 * @returns {Promise} 更新结果
 */
export function updatePaymentStatus(id, status) {
  return request.put(`/payment/${id}/status`, null, { params: { status } })
}

export function deletePayment(id) {
  return request.delete(`/payment/${id}`)
}
