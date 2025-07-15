package com.ruoyi.websocket.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 无人机信息对象 uav_vehicle
 * 
 * @author ruoyi
 * @date 2024-09-02
 */
public class WebSocketVehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 无人机设备ID */
    private Long vehicleId;

    /** 无人机mac地址 */
    @Excel(name = "无人机mac地址")
    private String mac;

    /** 无人机速度 km/h */
    @Excel(name = "无人机速度 km/h")
    private BigDecimal vehicleSpeed;

    /** 无人机电量 */
    @Excel(name = "无人机电量")
    private BigDecimal vehicleSoc;

    /** 无人机经度 */
    @Excel(name = "无人机经度")
    private BigDecimal vehicleLong;

    /** 无人机纬度 */
    @Excel(name = "无人机纬度")
    private BigDecimal vehicleLat;

    /** 无人机海拔高度 */
    @Excel(name = "无人机海拔高度")
    private BigDecimal vehicleAlt;

    /** 无人机当前模式 */
    @Excel(name = "无人机当前模式")
    private BigDecimal customMode;

    /** 错误信息 */
    @Excel(name = "错误信息")
    private int faultInfo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public BigDecimal getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(BigDecimal vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public BigDecimal getVehicleSoc() {
        return vehicleSoc;
    }

    public void setVehicleSoc(BigDecimal vehicleSoc) {
        this.vehicleSoc = vehicleSoc;
    }

    public BigDecimal getVehicleLong() {
        return vehicleLong;
    }

    public void setVehicleLong(BigDecimal vehicleLong) {
        this.vehicleLong = vehicleLong;
    }

    public BigDecimal getVehicleLat() {
        return vehicleLat;
    }

    public void setVehicleLat(BigDecimal vehicleLat) {
        this.vehicleLat = vehicleLat;
    }

    public BigDecimal getVehicleAlt() {
        return vehicleAlt;
    }

    public void setVehicleAlt(BigDecimal vehicleAlt) {
        this.vehicleAlt = vehicleAlt;
    }

    public BigDecimal getCustomMode() {
        return customMode;
    }

    public void setCustomMode(BigDecimal customMode) {
        this.customMode = customMode;
    }

    public int getFaultInfo() {
        return faultInfo;
    }

    public void setFaultInfo(int faultInfo) {
        this.faultInfo = faultInfo;
    }

    @Override
    public String toString() {
        return "WebSocketVehicle{" +
                "vehicleId=" + vehicleId +
                ", mac='" + mac + '\'' +
                ", vehicleSpeed=" + vehicleSpeed +
                ", vehicleSoc=" + vehicleSoc +
                ", vehicleLong=" + vehicleLong +
                ", vehicleLat=" + vehicleLat +
                ", vehicleAlt=" + vehicleAlt +
                ", customMode=" + customMode +
                ", faultInfo=" + faultInfo +
                '}';
    }
}
