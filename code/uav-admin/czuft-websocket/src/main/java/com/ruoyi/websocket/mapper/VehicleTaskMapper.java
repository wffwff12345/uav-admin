package com.ruoyi.websocket.mapper;

import com.ruoyi.websocket.domain.UavVehicleTask;

import java.util.List;

/**
 * 无人机任务信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public interface VehicleTaskMapper
{

    /**
     * 查询无人机任务信息列表
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 无人机任务信息集合
     */
    public List<UavVehicleTask> selectUavVehicleTaskList(UavVehicleTask uavVehicleTask);

    /**
     * 修改无人机任务信息
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    public int updateUavVehicleTask(UavVehicleTask uavVehicleTask);
}
