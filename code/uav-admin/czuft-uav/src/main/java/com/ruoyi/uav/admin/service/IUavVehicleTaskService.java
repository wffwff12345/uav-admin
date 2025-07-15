package com.ruoyi.uav.admin.service;

import com.ruoyi.websocket.domain.UavVehicleTask;

import java.util.List;

/**
 * 无人机任务信息Service接口
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public interface IUavVehicleTaskService 
{
    /**
     * 查询无人机任务信息
     * 
     * @param vehicleTaskId 无人机任务信息主键
     * @return 无人机任务信息
     */
    public UavVehicleTask selectUavVehicleTaskByVehicleTaskId(Long vehicleTaskId);

    /**
     * 查询无人机任务信息列表
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 无人机任务信息集合
     */
    public List<UavVehicleTask> selectUavVehicleTaskList(UavVehicleTask uavVehicleTask);

    /**
     * 查询无人机任务信息列表
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 无人机任务信息集合
     */
    public List<UavVehicleTask> selectUavVehicleTaskListByStatus(UavVehicleTask uavVehicleTask);

    /**
     * 查询任务航线初始化操作
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 无人机任务信息集合
     */
    public List<UavVehicleTask> selectBeforeUavVehicleTaskList(UavVehicleTask uavVehicleTask);

    /**
     * 新增无人机任务信息
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    public UavVehicleTask insertUavVehicleTask(UavVehicleTask uavVehicleTask);

    /**
     * 修改无人机任务信息
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    public int updateUavVehicleTask(UavVehicleTask uavVehicleTask);

    /**
     * 批量删除无人机任务信息
     * 
     * @param vehicleTaskIds 需要删除的无人机任务信息主键集合
     * @return 结果
     */
    public int deleteUavVehicleTaskByVehicleTaskIds(Long[] vehicleTaskIds);

    /**
     * 删除无人机任务信息信息
     * 
     * @param vehicleTaskId 无人机任务信息主键
     * @return 结果
     */
    public int deleteUavVehicleTaskByVehicleTaskId(Long vehicleTaskId);
}
