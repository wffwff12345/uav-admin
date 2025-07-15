package com.ruoyi.uav.admin.service.Impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.uav.admin.domain.UavVehicleTaskItem;
import com.ruoyi.uav.admin.mapper.UavVehicleTaskItemMapper;
import com.ruoyi.uav.admin.service.IUavVehicleTaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 无人机任务项信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
@Service
public class UavVehicleTaskItemServiceImpl implements IUavVehicleTaskItemService
{
    @Autowired
    private UavVehicleTaskItemMapper uavVehicleTaskItemMapper;

    /**
     * 查询无人机任务项信息
     * 
     * @param vehicleTaskItemId 无人机任务项信息主键
     * @return 无人机任务项信息
     */
    @Override
    public UavVehicleTaskItem selectUavVehicleTaskItemByVehicleTaskItemId(Long vehicleTaskItemId)
    {
        return uavVehicleTaskItemMapper.selectUavVehicleTaskItemByVehicleTaskItemId(vehicleTaskItemId);
    }

    /**
     * 查询无人机任务项信息列表
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 无人机任务项信息
     */
    @Override
    public List<UavVehicleTaskItem> selectUavVehicleTaskItemList(UavVehicleTaskItem uavVehicleTaskItem)
    {
        return uavVehicleTaskItemMapper.selectUavVehicleTaskItemList(uavVehicleTaskItem);
    }

    /**
     * 新增无人机任务项信息
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 结果
     */
    @Override
    public int insertUavVehicleTaskItem(UavVehicleTaskItem uavVehicleTaskItem)
    {
        uavVehicleTaskItem.setCreateTime(DateUtils.getNowDate());
        return uavVehicleTaskItemMapper.insertUavVehicleTaskItem(uavVehicleTaskItem);
    }

    /**
     * 修改无人机任务项信息
     * 
     * @param uavVehicleTaskItem 无人机任务项信息
     * @return 结果
     */
    @Override
    public int updateUavVehicleTaskItem(UavVehicleTaskItem uavVehicleTaskItem)
    {
        uavVehicleTaskItem.setUpdateTime(DateUtils.getNowDate());
        return uavVehicleTaskItemMapper.updateUavVehicleTaskItem(uavVehicleTaskItem);
    }

    /**
     * 批量删除无人机任务项信息
     * 
     * @param vehicleTaskItemIds 需要删除的无人机任务项信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleTaskItemByVehicleTaskItemIds(Long[] vehicleTaskItemIds)
    {
        return uavVehicleTaskItemMapper.deleteUavVehicleTaskItemByVehicleTaskItemIds(vehicleTaskItemIds);
    }

    /**
     * 删除无人机任务项信息信息
     * 
     * @param vehicleTaskItemId 无人机任务项信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleTaskItemByVehicleTaskItemId(Long vehicleTaskItemId)
    {
        return uavVehicleTaskItemMapper.deleteUavVehicleTaskItemByVehicleTaskItemId(vehicleTaskItemId);
    }

    @Override
    public int insertUavVehicleTaskItems(List<UavVehicleTaskItem> uavVehicleTaskItems) {
        return uavVehicleTaskItemMapper.insertUavVehicleTaskItems(uavVehicleTaskItems);
    }
}
