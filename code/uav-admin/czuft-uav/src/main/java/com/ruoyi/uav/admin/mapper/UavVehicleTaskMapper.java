package com.ruoyi.uav.admin.mapper;

import com.ruoyi.websocket.domain.UavVehicleTask;

import java.util.List;

/**
 * 无人机任务信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public interface UavVehicleTaskMapper 
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
     * 新增无人机任务信息
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    public int insertUavVehicleTask(UavVehicleTask uavVehicleTask);

    /**
     * 修改无人机任务信息
     * 
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    public int updateUavVehicleTask(UavVehicleTask uavVehicleTask);

    /**
     * 删除无人机任务信息
     * 
     * @param vehicleTaskId 无人机任务信息主键
     * @return 结果
     */
    public int deleteUavVehicleTaskByVehicleTaskId(Long vehicleTaskId);

    /**
     * 批量删除无人机任务信息
     * 
     * @param vehicleTaskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUavVehicleTaskByVehicleTaskIds(Long[] vehicleTaskIds);

    public List<UavVehicleTask> selectUavVehicleTaskListByStatus(UavVehicleTask uavVehicleTask);

    public List<UavVehicleTask> selectBeforeUavVehicleTaskList(UavVehicleTask uavVehicleTask);
}
