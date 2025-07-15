import request from '@/utils/request'

// 查询无人机实时信息列表
export function listInfo(query) {
  return request({
    url: '/admin/uavVehicleInfo/list',
    method: 'get',
    params: query
  })
}

// 查询无人机实时信息详细
export function getInfo(vehicleInfoId) {
  return request({
    url: '/admin/uavVehicleInfo' + vehicleInfoId,
    method: 'get'
  })
}

// 新增无人机实时信息
export function addInfo(data) {
  return request({
    url: '/admin/uavVehicleInfo',
    method: 'post',
    data: data
  })
}

// 修改无人机实时信息
export function updateInfo(data) {
  return request({
    url: '/admin/uavVehicleInfo',
    method: 'put',
    data: data
  })
}

// 删除无人机实时信息
export function delInfo(vehicleInfoId) {
  return request({
    url: '/admin/uavVehicleInfos' + vehicleInfoId,
    method: 'delete'
  })
}
