package com.ruoyi.websocket.domain;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 无人机基础信息管理对象 uav_vehicle
 * 
 * @author ruoyi
 * @date 2024-09-03
 */
public class Vehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 无人机id */
    private Long vehicleId;

    /** 无人机名称 */
    @Excel(name = "无人机名称")
    private String vehicleName;

    /** 无人机ip地址 */
    @Excel(name = "无人机mac地址")
    private String vehicleIp;

    /** 无人机port端口 */
    @Excel(name = "无人机port端口")
    private String vehiclePort;

    /** 无人机视频流地址 */
    @Excel(name = "无人机视频流地址")
    private String vehicleRtsp;

    /** 无人机所属组ID */
    @Excel(name = "无人机所属组ID")
    private String vehicleGroupId;

    /** 无人机所属组名 */
    @Excel(name = "无人机所属组名")
    private String vehicleGroupName;

    /** 无人机在线状态 0: 离线, 1: 在线*/
    @Excel(name = "无人机在线状态")
    private String vehicleOnlineStatus;

    /** 无人机状态 0: 未连接, 1: 已连接*/
    @Excel(name = "无人机状态")
    private String vehicleStatus;

    /** 无人机系统ID*/
    @Excel(name = "无人机系统ID")
    private Long sysId;

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleIp() {
        return vehicleIp;
    }

    public void setVehicleIp(String vehicleIp) {
        this.vehicleIp = vehicleIp;
    }

    public String getVehiclePort() {
        return vehiclePort;
    }

    public void setVehiclePort(String vehiclePort) {
        this.vehiclePort = vehiclePort;
    }

    public String getVehicleRtsp() {
        return vehicleRtsp;
    }

    public void setVehicleRtsp(String vehicleRtsp) {
        this.vehicleRtsp = vehicleRtsp;
    }

    public String getVehicleGroupId() {
        return vehicleGroupId;
    }

    public void setVehicleGroupId(String vehicleGroupId) {
        this.vehicleGroupId = vehicleGroupId;
    }

    public String getVehicleGroupName() {
        return vehicleGroupName;
    }

    public void setVehicleGroupName(String vehicleGroupName) {
        this.vehicleGroupName = vehicleGroupName;
    }

    public String getVehicleOnlineStatus() {
        return vehicleOnlineStatus;
    }

    public void setVehicleOnlineStatus(String vehicleOnlineStatus) {
        this.vehicleOnlineStatus = vehicleOnlineStatus;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Long getSysId() {
        return sysId;
    }

    public void setSysId(Long sysId) {
        this.sysId = sysId;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleName='" + vehicleName + '\'' +
                ", vehicleIp='" + vehicleIp + '\'' +
                ", vehiclePort='" + vehiclePort + '\'' +
                ", vehicleRtsp='" + vehicleRtsp + '\'' +
                ", vehicleGroupId='" + vehicleGroupId + '\'' +
                ", vehicleGroupName='" + vehicleGroupName + '\'' +
                ", vehicleOnlineStatus='" + vehicleOnlineStatus + '\'' +
                ", vehicleStatus='" + vehicleStatus + '\'' +
                ", sysId=" + sysId +
                '}';
    }
}
