package com.ruoyi.websocket.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 无人机任务信息对象 uav_vehicle_task
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
public class UavVehicleTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 无人机任务ID */
    private Long vehicleTaskId;

    /** 无人机任务名称 */
    @Excel(name = "无人机任务名称")
    private String vehicleTaskName;

    /** 无人机任务执行时间 */
    @Excel(name = "无人机任务执行时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vehicleTaskDatetime;

    /** 无人机ID */
    @Excel(name = "无人机ID")
    private Long vehicleId;

    /** 无人机系统ID */
    @Excel(name = "无人机系统ID")
    private Integer sysId;

    /** 无人机任务状态： 0：未完成，1：已完成*/
    @Excel(name = "无人机任务状态： 0：未完成,1：已完成")
    private String vehicleTaskStatus;

    /** 最后一个动作 0: 返航 1：原地降落 */
    @Excel(name = "无人机终点任务")
    private Integer nextAction;

    /** 任务是否可执行：0：任务不可执行,1：任务可执行 */
    @Excel(name = "任务是否可执行：0：任务不可执行,1：任务可执行")
    private String isAvailable;

    public Long getVehicleTaskId() {
        return vehicleTaskId;
    }

    public void setVehicleTaskId(Long vehicleTaskId) {
        this.vehicleTaskId = vehicleTaskId;
    }

    public String getVehicleTaskName() {
        return vehicleTaskName;
    }

    public void setVehicleTaskName(String vehicleTaskName) {
        this.vehicleTaskName = vehicleTaskName;
    }

    public Date getVehicleTaskDatetime() {
        return vehicleTaskDatetime;
    }

    public void setVehicleTaskDatetime(Date vehicleTaskDatetime) {
        this.vehicleTaskDatetime = vehicleTaskDatetime;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getSysId() {
        return sysId;
    }

    public void setSysId(Integer sysId) {
        this.sysId = sysId;
    }

    public String getVehicleTaskStatus() {
        return vehicleTaskStatus;
    }

    public void setVehicleTaskStatus(String vehicleTaskStatus) {
        this.vehicleTaskStatus = vehicleTaskStatus;
    }

    public Integer getNextAction() {
        return nextAction;
    }

    public void setNextAction(Integer nextAction) {
        this.nextAction = nextAction;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String is_available) {
        this.isAvailable = is_available;
    }

    @Override
    public String toString() {
        return "UavVehicleTask{" +
                "vehicleTaskId=" + vehicleTaskId +
                ", vehicleTaskName='" + vehicleTaskName + '\'' +
                ", vehicleTaskDatetime=" + vehicleTaskDatetime +
                ", vehicleId='" + vehicleId + '\'' +
                ", sysId=" + sysId +
                ", vehicleTaskStatus='" + vehicleTaskStatus + '\'' +
                ", nextAction=" + nextAction +
                ", isAvailable='" + isAvailable + '\'' +
                '}';
    }
}
