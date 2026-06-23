/**
 * 病历管理 API 模块
 *
 * 封装病历相关的接口：
 * - 分页查询病历列表（GET /api/medical-record/list）
 * - 根据 ID 查询病历详情（GET /api/medical-record/{id}）
 * - 根据挂号 ID 查询病历（GET /api/medical-record/registration/{regId}）
 * - 新增病历（POST /api/medical-record）
 * - 修改病历（PUT /api/medical-record/{id}）
 */

import request from '@/utils/request'

/**
 * 分页查询病历列表
 * @param {object} params 查询参数（page, pageSize, patientId, doctorId 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getMedicalRecordList(params) {
  return request.get('/medical-record/list', { params })
}

/**
 * 根据 ID 查询病历详情
 * @param {number|string} id 病历 ID
 * @returns {Promise} 病历详细信息
 */
export function getMedicalRecordById(id) {
  return request.get(`/medical-record/${id}`)
}

/**
 * 根据挂号 ID 查询对应的病历
 * @param {number|string} regId 挂号记录 ID
 * @returns {Promise} 病历信息
 */
export function getMedicalRecordByRegistration(regId) {
  return request.get(`/medical-record/registration/${regId}`)
}

/**
 * 新增病历
 * @param {object} data 病历信息（regId, patientId, doctorId, chiefComplaint, diagnosis, treatment 等）
 * @returns {Promise} 新增结果
 */
export function addMedicalRecord(data) {
  return request.post('/medical-record', data)
}

/**
 * 修改病历
 * @param {number|string} id 病历 ID
 * @param {object} data 更新的病历信息
 * @returns {Promise} 更新结果
 */
export function updateMedicalRecord(id, data) {
  return request.put(`/medical-record/${id}`, data)
}
