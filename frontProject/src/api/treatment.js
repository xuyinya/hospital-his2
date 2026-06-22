import request from '@/utils/request'

export function getTreatmentList(params) {
  return request.get('/treatment/list', { params })
}

export function getTreatmentById(id) {
  return request.get(`/treatment/${id}`)
}

export function addTreatment(data) {
  return request.post('/treatment', data)
}

export function updateTreatment(id, data) {
  return request.put(`/treatment/${id}`, data)
}

export function updateTreatmentStatus(id, status) {
  return request.put(`/treatment/${id}/status`, null, { params: { status } })
}
