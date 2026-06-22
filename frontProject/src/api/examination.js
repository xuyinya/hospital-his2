import request from '@/utils/request'

export function getExaminationList(params) {
  return request.get('/examination/list', { params })
}

export function getExaminationById(id) {
  return request.get(`/examination/${id}`)
}

export function addExamination(data) {
  return request.post('/examination', data)
}

export function updateExamination(id, data) {
  return request.put(`/examination/${id}`, data)
}

export function updateExaminationResult(id, result) {
  return request.put(`/examination/${id}/result`, { result })
}
