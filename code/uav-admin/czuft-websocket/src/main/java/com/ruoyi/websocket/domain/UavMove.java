package com.ruoyi.websocket.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UavMove {
    // 无人机fid地址
    public int fid;
    // 飞行高度
    public double Alt;
    //纬度
    public double latitude;
    //经度
    public double longitude;
}
