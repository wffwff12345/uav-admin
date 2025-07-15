package com.ruoyi.websocket.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 无人机实时状态信息管理对象 vehicle_info
 *
 * @author ruoyi
 * @date 2024-09-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfo extends BaseEntity{

    private static final long serialVersionUID = 1L;

    //无人机实时状态信息ID
    private Long vehicleInfoId;

    //无人机ID
    private Long vehicleId;

    //无人机系统ID
    @JsonProperty("fid")
    public int sysId;

    //地速
    public double groundSpeed;

    //电量
    @JsonProperty("vehicleSoc")
    public int soc;

    //经度
    public double lng;

    //纬度
    public double lat;

    //高度
    @JsonProperty("Alt")
    public double alt;

    //飞行时间
    public int flyInAir;

    //飞机航向
    public float headAngle;

    //GPS定位状态：0:nogps; 1:nofix; 2:fix; 3,4:3d fix; 5:rtk; 6:rtkfix
    //GPS值>=2就可以执行后续操作
    @JsonProperty("gpsStatu")
    public int gpsStatus;

    //卫星数：定位质量
    @JsonProperty("satcount")
    public int satCount;

    // 飞行模式
    public String mode;
}
