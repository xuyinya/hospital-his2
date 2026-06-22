import request from '@/utils/request'

export function getPatientList(params) {
  return request.get('/patient/list', { params })
}

export function getPatientById(id) {
  return request.get(`/patient/${id}`)
}

export function addPatient(data) {
  return request.post('/patient', data)
}

export function updatePatient(id, data) {
  return request.put(`/patient/${id}`, data)
}
