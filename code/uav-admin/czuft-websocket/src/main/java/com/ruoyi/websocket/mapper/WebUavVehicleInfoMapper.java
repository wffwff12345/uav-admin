package com.ruoyi.websocket.mapper;
import com.ruoyi.websocket.domain.Vehicle;
import com.ruoyi.websocket.domain.VehicleInfo;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 无人机基础信息管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-03
 */
public interface WebUavVehicleInfoMapper
{
    /**
     * 批量新增无人机实时信息
     *
     * @param vehicleList 需要新增的无人机实时信息集合
     * @return 结果
     */
    public void saveBatch(@Param("list") List<VehicleInfo> vehicleList);
}
