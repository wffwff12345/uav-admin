package com.ruoyi.websocket.service.Impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.websocket.domain.UavVehicleTask;
import com.ruoyi.websocket.mapper.VehicleTaskMapper;
import com.ruoyi.websocket.service.IVehicleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 无人机任务信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-07
 */
@Service
public class VehicleTaskServiceImpl implements IVehicleTaskService {
    @Autowired
    private VehicleTaskMapper uavVehicleTaskMapper;


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
}
