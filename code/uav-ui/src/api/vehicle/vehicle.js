import request from '@/utils/request'

// 查询无人机信息列表
export function listVehicle(query) {
  return request({
    url: '/system/vehicle/list',
    method: 'get',
    params: query
  })
}

// 查询无人机信息详细
export function getVehicle(vehicleId) {
  return request({
    url: '/system/vehicle/' + vehicleId,
    method: 'get'
  })
}

// 新增无人机信息
export function addVehicle(data) {
  return request({
    url: '/system/vehicle',
    method: 'post',
    data: data
  })
}

// 修改无人机信息
export function updateVehicle(data) {
  return request({
    url: '/system/vehicle',
    method: 'put',
    data: data
  })
}

// 删除无人机信息
export function delVehicle(vehicleId) {
  return request({
    url: '/system/vehicle/' + vehicleId,
    method: 'delete'
  })
}
