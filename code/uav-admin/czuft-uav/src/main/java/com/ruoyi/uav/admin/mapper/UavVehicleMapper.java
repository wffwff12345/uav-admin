package com.ruoyi.uav.admin.mapper;

import com.ruoyi.websocket.domain.Vehicle;

import java.util.List;

/**
 * 无人机信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-02
 */
public interface UavVehicleMapper 
{
    /**
     * 查询无人机信息
     * 
     * @param vehicleId 无人机信息主键
     * @return 无人机信息
     */
    public Vehicle selectUavVehicleByVehicleId(Long vehicleId);

    /**
     * 查询无人机信息列表
     * 
     * @param vehicle 无人机信息
     * @return 无人机信息集合
     */
    public List<Vehicle> selectUavVehicleList(Vehicle vehicle);

    /**
     * 新增无人机信息
     * 
     * @param vehicle 无人机信息
     * @return 结果
     */
    public int insertUavVehicle(Vehicle vehicle);

    /**
     * 修改无人机信息
     * 
     * @param vehicle 无人机信息
     * @return 结果
     */
    public int updateUavVehicle(Vehicle vehicle);

    /**
     * 删除无人机信息
     * 
     * @param vehicleId 无人机信息主键
     * @return 结果
     */
    public int deleteUavVehicleByVehicleId(Long vehicleId);

    /**
     * 批量删除无人机信息
     * 
     * @param vehicleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUavVehicleByVehicleIds(Long[] vehicleIds);
}
