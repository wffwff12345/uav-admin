package com.ruoyi.uav.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 无人机任务项信息对象 uav_vehicle_task_item
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public class UavVehicleTaskItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 无人机任务项ID */
    private Long vehicleTaskItemId;

    /** 无人机任务点 */
    @Excel(name = "无人机任务点")
    private String vehicleTaskItem;

    /** 无人机任务ID */
    @Excel(name = "无人机任务ID")
    private Long vehicleTaskId;

    public void setVehicleTaskItemId(Long vehicleTaskItemId) 
    {
        this.vehicleTaskItemId = vehicleTaskItemId;
    }

    public Long getVehicleTaskItemId() 
    {
        return vehicleTaskItemId;
    }
    public void setVehicleTaskItem(String vehicleTaskItem) 
    {
        this.vehicleTaskItem = vehicleTaskItem;
    }

    public String getVehicleTaskItem() 
    {
        return vehicleTaskItem;
    }
    public void setVehicleTaskId(Long vehicleTaskId) 
    {
        this.vehicleTaskId = vehicleTaskId;
    }

    public Long getVehicleTaskId() 
    {
        return vehicleTaskId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("vehicleTaskItemId", getVehicleTaskItemId())
            .append("vehicleTaskItem", getVehicleTaskItem())
            .append("vehicleTaskId", getVehicleTaskId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
