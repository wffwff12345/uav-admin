package com.ruoyi.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraFollow extends CameraDto{
    private int pixelH; // 跟踪目标中心水平坐标，中间为 0，左负右正
    private int pixelV; // 跟踪目标中心垂直坐标中间为 0，上正下负
    private int enable; // 跟踪目标中心垂直坐标中间为 0，上正下负
}
