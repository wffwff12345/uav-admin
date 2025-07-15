package com.ruoyi.uav.admin.service.Impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.uav.admin.mapper.UavVehicleMapper;
import com.ruoyi.uav.admin.service.IUavVehicleService;
import com.ruoyi.websocket.domain.Vehicle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-02
 */
@Service
public class UavVehicleServiceImpl implements IUavVehicleService
{
    @Resource
    private UavVehicleMapper uavVehicleMapper;

    /**
     * 查询无人机信息
     * 
     * @param vehicleId 无人机信息主键
     * @return 无人机信息
     */
    @Override
    public Vehicle selectUavVehicleByVehicleId(Long vehicleId)
    {
        return uavVehicleMapper.selectUavVehicleByVehicleId(vehicleId);
    }

    /**
     * 查询无人机信息列表
     * 
     * @param vehicle 无人机信息
     * @return 无人机信息
     */
    @Override
    public List<Vehicle> selectUavVehicleList(Vehicle vehicle)
    {
        return uavVehicleMapper.selectUavVehicleList(vehicle);
    }

    /**
     * 新增无人机信息
     * 
     * @param vehicle 无人机信息
     * @return 结果
     */
    @Override
    public int insertUavVehicle(Vehicle vehicle)
    {
        vehicle.setCreateTime(DateUtils.getNowDate());
        return uavVehicleMapper.insertUavVehicle(vehicle);
    }

    /**
     * 修改无人机信息
     * 
     * @param vehicle 无人机信息
     * @return 结果
     */
    @Override
    public int updateUavVehicle(Vehicle vehicle)
    {
        vehicle.setUpdateTime(DateUtils.getNowDate());
        return uavVehicleMapper.updateUavVehicle(vehicle);
    }

    /**
     * 批量删除无人机信息
     * 
     * @param vehicleIds 需要删除的无人机信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleByVehicleIds(Long[] vehicleIds)
    {
        return uavVehicleMapper.deleteUavVehicleByVehicleIds(vehicleIds);
    }

    /**
     * 删除无人机信息信息
     * 
     * @param vehicleId 无人机信息主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleByVehicleId(Long vehicleId)
    {
        return uavVehicleMapper.deleteUavVehicleByVehicleId(vehicleId);
    }
}
