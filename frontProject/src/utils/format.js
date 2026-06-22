/**
 * 格式化日期时间
 * @param {string|Date} date
 * @returns {string}
 */
export function formatDateTime(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

/**
 * 格式化日期
 * @param {string|Date} date
 * @returns {string}
 */
export function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 格式化金额
 * @param {number} amount
 * @returns {string}
 */
export function formatMoney(amount) {
  if (amount === null || amount === undefined) return '0.00'
  return Number(amount).toFixed(2)
}

/**
 * 性别格式化
 * @param {number} gender
 * @returns {string}
 */
export function formatGender(gender) {
  return gender === 1 ? '男' : '女'
}

/**
 * 状态格式化
 * @param {number} status
 * @param {object} map
 * @returns {string}
 */
export function formatStatus(status, map = {}) {
  return map[status] || '未知'
}

/**
 * 挂号状态
 */
export const regStatusMap = {
  0: '待诊',
  1: '已诊',
  2: '已取消'
}

/**
 * 处方状态
 */
export const prescriptionStatusMap = {
  0: '未取药',
  1: '已取药'
}

/**
 * 支付状态
 */
export const paymentStatusMap = {
  0: '未支付',
  1: '已支付'
}
