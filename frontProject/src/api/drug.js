import request from '@/utils/request'

export function getDrugList(params) {
  return request.get('/drug/list', { params })
}

export function getDrugById(id) {
  return request.get(`/drug/${id}`)
}

export function addDrug(data) {
  return request.post('/drug', data)
}

export function updateDrug(id, data) {
  return request.put(`/drug/${id}`, data)
}

export function deleteDrug(id) {
  return request.delete(`/drug/${id}`)
}
