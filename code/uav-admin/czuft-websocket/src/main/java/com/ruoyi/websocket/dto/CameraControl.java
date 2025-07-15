package com.ruoyi.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraControl extends CameraDto{
    private int angle; // 视场角角度，-1为缩小，1为放大
    private int infrared; // 可见光与红外切换，0 可见光，1 红外
    private int laser; // 是否开启激光测距，0 关闭，1 开启
}
