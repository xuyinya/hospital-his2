import request from '@/utils/request'

// 获取患者个人信息
export function getPatientInfo() {
  return request.get('/auth/info')
}

// 获取患者的挂号记录
export function getMyRegistrations(params) {
  return request.get('/registration/list', { params })
}

// 获取患者的病历
export function getMyRecords(params) {
  return request.get('/medical-record/list', { params })
}

// 获取患者的处方
export function getMyPrescriptions(params) {
  return request.get('/prescription/list', { params })
}

// 获取处方明细
export function getMyPrescriptionDetails(id) {
  return request.get(`/prescription/${id}/details`)
}
