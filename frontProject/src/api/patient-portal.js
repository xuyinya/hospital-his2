/**
 * 患者门户 API 模块
 *
 * 封装患者端自助服务相关的接口：
 * - 获取当前患者信息（GET /api/auth/info）
 * - 获取患者的挂号记录（GET /api/registration/list）
 * - 获取患者的病历记录（GET /api/medical-record/list）
 * - 获取患者的处方列表（GET /api/prescription/list）
 * - 获取处方明细（GET /api/prescription/{id}/details）
 */

import request from '@/utils/request'

/**
 * 获取患者个人信息
 * @returns {Promise} 患者的基本信息
 */
export function getPatientInfo() {
  return request.get('/auth/info')
}

/**
 * 获取患者的挂号记录
 * @param {object} params 查询参数（page, pageSize 等）
 * @returns {Promise} 分页的挂号记录列表
 */
export function getMyRegistrations(params) {
  return request.get('/registration/list', { params })
}

/**
 * 获取患者的病历记录
 * @param {object} params 查询参数（page, pageSize 等）
 * @returns {Promise} 分页的病历记录列表
 */
export function getMyRecords(params) {
  return request.get('/medical-record/list', { params })
}

/**
 * 获取患者的处方列表
 * @param {object} params 查询参数（page, pageSize 等）
 * @returns {Promise} 分页的处方列表
 */
export function getMyPrescriptions(params) {
  return request.get('/prescription/list', { params })
}

/**
 * 获取处方明细（药品明细）
 * @param {number|string} id 处方 ID
 * @returns {Promise} 处方的药品明细列表
 */
export function getMyPrescriptionDetails(id) {
  return request.get(`/prescription/${id}/details`)
}

/**
 * 患者自助挂号
 * 患者自行选择科室、医生和挂号类型完成挂号，患者ID从JWT中自动获取
 * @param {object} data 挂号信息（deptId, doctorId, regType）
 * @returns {Promise} 操作结果
 */
export function selfRegistration(data) {
  return request.post('/registration/self', data)
}
