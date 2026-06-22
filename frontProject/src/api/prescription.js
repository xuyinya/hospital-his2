import request from '@/utils/request'

export function getPrescriptionList(params) {
  return request.get('/prescription/list', { params })
}

export function getPrescriptionById(id) {
  return request.get(`/prescription/${id}`)
}

export function getPrescriptionDetails(id) {
  return request.get(`/prescription/${id}/details`)
}

export function addPrescription(data) {
  return request.post('/prescription', data)
}

export function updatePrescriptionStatus(id, status) {
  return request.put(`/prescription/${id}/status`, null, { params: { status } })
}
