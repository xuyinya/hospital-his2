/**
 * 医生管理 API 模块
 *
 * 封装医生信息相关的接口：
 * - 分页查询医生列表（GET /api/doctor/list）
 * - 根据 ID 查询医生详情（GET /api/doctor/{id}）
 * - 根据科室 ID 查询医生（GET /api/doctor/dept/{deptId}）
 * - 新增医生（POST /api/doctor）
 * - 修改医生信息（PUT /api/doctor/{id}）
 * - 删除医生（DELETE /api/doctor/{id}）
 */

import request from '@/utils/request'

/**
 * 分页查询医生列表
 * @param {object} params 查询参数（page, pageSize, name, deptId 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getDoctorList(params) {
  return request.get('/doctor/list', { params })
}

/**
 * 根据 ID 查询医生详情
 * @param {number|string} id 医生 ID
 * @returns {Promise} 医生详细信息
 */
export function getDoctorById(id) {
  return request.get(`/doctor/${id}`)
}

/**
 * 根据科室 ID 查询该科室下的所有医生
 * @param {number|string} deptId 科室 ID
 * @returns {Promise} 医生列表
 */
export function getDoctorByDept(deptId) {
  return request.get(`/doctor/dept/${deptId}`)
}

/**
 * 新增医生
 * @param {object} data 医生信息（name, deptId, title 等）
 * @returns {Promise} 新增结果
 */
export function addDoctor(data) {
  return request.post('/doctor', data)
}

/**
 * 修改医生信息
 * @param {number|string} id 医生 ID
 * @param {object} data 更新的医生信息
 * @returns {Promise} 更新结果
 */
export function updateDoctor(id, data) {
  return request.put(`/doctor/${id}`, data)
}

/**
 * 删除医生
 * @param {number|string} id 医生 ID
 * @returns {Promise} 删除结果
 */
export function deleteDoctor(id) {
  return request.delete(`/doctor/${id}`)
}
