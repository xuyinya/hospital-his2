import request from '@/utils/request'

export function getDoctorList(params) {
  return request.get('/doctor/list', { params })
}

export function getDoctorById(id) {
  return request.get(`/doctor/${id}`)
}

export function getDoctorByDept(deptId) {
  return request.get(`/doctor/dept/${deptId}`)
}

export function addDoctor(data) {
  return request.post('/doctor', data)
}

export function updateDoctor(id, data) {
  return request.put(`/doctor/${id}`, data)
}

export function deleteDoctor(id) {
  return request.delete(`/doctor/${id}`)
}
