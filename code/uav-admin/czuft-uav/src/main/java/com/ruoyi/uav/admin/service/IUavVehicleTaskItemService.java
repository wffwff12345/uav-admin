package com.ruoyi.uav.admin.service;

import java.util.List;
import com.ruoyi.uav.admin.domain.UavVehicleTaskItem;

/**
 * 无人机任务项信息Service接口
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public interface IUavVehicleTaskItemService 
{
    /**
     * 查询无人机任务项信息
     * 
     * @param vehicleTaskItemId 无人机任务项信息主键
     * @return 无人机任务项信息
     */
    public UavVehicleTaskItem selectUavVehicleTaskItemByVehicleTaskItemId(Long vehicleTaskItemId);

    /**
     * 查询无人机任务项信息列表
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 无人机任务项信息集合
     */
    public List<UavVehicleTaskItem> selectUavVehicleTaskItemList(UavVehicleTaskItem uavVehicleTaskItem);

    /**
     * 新增无人机任务项信息
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 结果
     */
    public int insertUavVehicleTaskItem(UavVehicleTaskItem uavVehicleTaskItem);

    /**
     * 修改无人机任务项信息
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 结果
     */
    public int updateUavVehicleTaskItem(UavVehicleTaskItem uavVehicleTaskItem);

    /**
     * 批量删除无人机任务项信息
     * 
     * @param vehicleTaskItemIds 需要删除的无人机任务项信息主键集合
     * @return 结果
     */
    public int deleteUavVehicleTaskItemByVehicleTaskItemIds(Long[] vehicleTaskItemIds);

    /**
     * 删除无人机任务项信息信息
     * 
     * @param vehicleTaskItemId 无人机任务项信息主键
     * @return 结果
     */
    public int deleteUavVehicleTaskItemByVehicleTaskItemId(Long vehicleTaskItemId);

    public int insertUavVehicleTaskItems(List<UavVehicleTaskItem> uavVehicleTaskItems);
}
