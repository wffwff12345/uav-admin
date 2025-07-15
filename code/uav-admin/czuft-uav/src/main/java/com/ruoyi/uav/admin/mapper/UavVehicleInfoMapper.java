package com.ruoyi.uav.admin.mapper;

import com.ruoyi.websocket.domain.VehicleInfo;

import java.util.List;

/**
 * 无人机基础信息管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-03
 */
public interface UavVehicleInfoMapper 
{
    /**
     * 查询无人机基础信息管理
     * 
     * @param vehicleId 无人机基础信息管理主键
     * @return 无人机基础信息管理
     */
    public VehicleInfo selectUavVehicleInfoByVehicleId(Long vehicleId);

    /**
     * 查询无人机基础信息管理列表
     * 
     * @param vehicleInfo 无人机基础信息管理
     * @return 无人机基础信息管理集合
     */
    public List<VehicleInfo> selectUavVehicleInfoList(VehicleInfo vehicleInfo);

    /**
     * 新增无人机基础信息管理
     * 
     * @param vehicleInfo 无人机基础信息管理
     * @return 结果
     */
    public int insertUavVehicleInfo(VehicleInfo vehicleInfo);

    /**
     * 修改无人机基础信息管理
     * 
     * @param vehicleInfo 无人机基础信息管理
     * @return 结果
     */
    public int updateUavVehicleInfo(VehicleInfo vehicleInfo);

    /**
     * 删除无人机基础信息管理
     * 
     * @param vehicleId 无人机基础信息管理主键
     * @return 结果
     */
    public int deleteUavVehicleInfoByVehicleId(Long vehicleId);

    /**
     * 批量删除无人机基础信息管理
     * 
     * @param vehicleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUavVehicleInfoByVehicleIds(Long[] vehicleIds);
}
