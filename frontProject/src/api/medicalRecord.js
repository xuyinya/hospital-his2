import request from '@/utils/request'

export function getMedicalRecordList(params) {
  return request.get('/medical-record/list', { params })
}

export function getMedicalRecordById(id) {
  return request.get(`/medical-record/${id}`)
}

export function getMedicalRecordByRegistration(regId) {
  return request.get(`/medical-record/registration/${regId}`)
}

export function addMedicalRecord(data) {
  return request.post('/medical-record', data)
}

export function updateMedicalRecord(id, data) {
  return request.put(`/medical-record/${id}`, data)
}
