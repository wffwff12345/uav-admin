package com.ruoyi.uav.admin.service.Impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.uav.admin.mapper.UavVehicleTaskMapper;
import com.ruoyi.uav.admin.service.IUavVehicleTaskService;
import com.ruoyi.websocket.domain.UavVehicleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 无人机任务信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-07
 */
@Service
public class UavVehicleTaskServiceImpl implements IUavVehicleTaskService {
    @Autowired
    private UavVehicleTaskMapper uavVehicleTaskMapper;

    /**
     * 查询无人机任务信息
     *
     * @param vehicleTaskId 无人机任务信息主键
     * @return 无人机任务信息
     */
    @Override
    public UavVehicleTask selectUavVehicleTaskByVehicleTaskId(Long vehicleTaskId) {
        return uavVehicleTaskMapper.selectUavVehicleTaskByVehicleTaskId(vehicleTaskId);
    }

    /**
     * 查询无人机任务信息列表
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 无人机任务信息
     */
    @Override
    public List<UavVehicleTask> selectUavVehicleTaskList(UavVehicleTask uavVehicleTask) {
        return uavVehicleTaskMapper.selectUavVehicleTaskList(uavVehicleTask);
    }

    @Override
    public List<UavVehicleTask> selectUavVehicleTaskListByStatus(UavVehicleTask uavVehicleTask) {
        return uavVehicleTaskMapper.selectUavVehicleTaskListByStatus(uavVehicleTask);
    }

    @Override
    public List<UavVehicleTask> selectBeforeUavVehicleTaskList(UavVehicleTask uavVehicleTask) {
        return uavVehicleTaskMapper.selectBeforeUavVehicleTaskList(uavVehicleTask);
    }

    /**
     * 新增无人机任务信息
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    @Override
    public UavVehicleTask insertUavVehicleTask(UavVehicleTask uavVehicleTask) {
        uavVehicleTask.setCreateTime(DateUtils.getNowDate());
        System.out.println("uavVehicleTask  uavVehicleTask"+uavVehicleTask.toString());
        uavVehicleTaskMapper.insertUavVehicleTask(uavVehicleTask);
        return uavVehicleTask;
    }

    /**
     * 修改无人机任务信息
     *
     * @param uavVehicleTask 无人机任务信息
     * @return 结果
     */
    @Override
    public int updateUavVehicleTask(UavVehicleTask uavVehicleTask) {
        uavVehicleTask.setUpdateTime(DateUtils.getNowDate());
        return uavVehicleTaskMapper.updateUavVehicleTask(uavVehicleTask);
    }

    /**
     * 批量删除无人机任务信息
     *
     * @param vehicleTaskIds 需要删除的无人机任务信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleTaskByVehicleTaskIds(Long[] vehicleTaskIds) {
        return uavVehicleTaskMapper.deleteUavVehicleTaskByVehicleTaskIds(vehicleTaskIds);
    }

    /**
     * 删除无人机任务信息信息
     *
     * @param vehicleTaskId 无人机任务信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleTaskByVehicleTaskId(Long vehicleTaskId) {
        return uavVehicleTaskMapper.deleteUavVehicleTaskByVehicleTaskId(vehicleTaskId);
    }
}
