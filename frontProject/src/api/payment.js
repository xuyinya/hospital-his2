import request from '@/utils/request'

export function getPaymentList(params) {
  return request.get('/payment/list', { params })
}

export function getPaymentById(id) {
  return request.get(`/payment/${id}`)
}

export function addPayment(data) {
  return request.post('/payment', data)
}

export function updatePaymentStatus(id, status) {
  return request.put(`/payment/${id}/status`, null, { params: { status } })
}
