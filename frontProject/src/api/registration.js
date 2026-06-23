/**
 * 挂号管理 API 模块
 *
 * 封装挂号相关的接口：
 * - 分页查询挂号记录（GET /api/registration/list）
 * - 根据 ID 查询挂号详情（GET /api/registration/{id}）
 * - 新增挂号（POST /api/registration）
 * - 修改挂号记录（PUT /api/registration/{id}）
 * - 删除挂号记录（DELETE /api/registration/{id}）
 * - 更新挂号状态（PUT /api/registration/{id}/status）
 */

import request from '@/utils/request'

/**
 * 分页查询挂号记录列表
 * @param {object} params 查询参数（page, pageSize, patientId, doctorId, deptId, status 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getRegistrationList(params) {
  return request.get('/registration/list', { params })
}

/**
 * 根据 ID 查询挂号详情
 * @param {number|string} id 挂号记录 ID
 * @returns {Promise} 挂号详细信息
 */
export function getRegistrationById(id) {
  return request.get(`/registration/${id}`)
}

/**
 * 新增挂号记录
 * @param {object} data 挂号信息（patientId, deptId, doctorId, regDate, regType, fee 等）
 * @returns {Promise} 新增结果
 */
export function addRegistration(data) {
  return request.post('/registration', data)
}

/**
 * 修改挂号记录
 * @param {number|string} id 挂号记录 ID
 * @param {object} data 更新的挂号信息
 * @returns {Promise} 更新结果
 */
export function updateRegistration(id, data) {
  return request.put(`/registration/${id}`, data)
}

/**
 * 删除挂号记录
 * @param {number|string} id 挂号记录 ID
 * @returns {Promise} 删除结果
 */
export function deleteRegistration(id) {
  return request.delete(`/registration/${id}`)
}

/**
 * 更新挂号状态（待诊 / 已诊 / 已取消）
 * @param {number|string} id 挂号记录 ID
 * @param {number|string} status 状态值（0=待诊, 1=已诊, 2=已取消）
 * @returns {Promise} 更新结果
 */
export function updateRegistrationStatus(id, status) {
  return request.put(`/registration/${id}/status`, null, { params: { status } })
}
