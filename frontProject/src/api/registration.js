import request from '@/utils/request'

export function getRegistrationList(params) {
  return request.get('/registration/list', { params })
}

export function getRegistrationById(id) {
  return request.get(`/registration/${id}`)
}

export function addRegistration(data) {
  return request.post('/registration', data)
}

export function updateRegistration(id, data) {
  return request.put(`/registration/${id}`, data)
}

export function deleteRegistration(id) {
  return request.delete(`/registration/${id}`)
}

export function updateRegistrationStatus(id, status) {
  return request.put(`/registration/${id}/status`, null, { params: { status } })
}
