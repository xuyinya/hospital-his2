import request from '@/utils/request'

export function getLaboratoryList(params) {
  return request.get('/laboratory/list', { params })
}

export function getLaboratoryById(id) {
  return request.get(`/laboratory/${id}`)
}

export function addLaboratory(data) {
  return request.post('/laboratory', data)
}

export function updateLaboratory(id, data) {
  return request.put(`/laboratory/${id}`, data)
}

export function updateLaboratoryResult(id, result) {
  return request.put(`/laboratory/${id}/result`, { result })
}
