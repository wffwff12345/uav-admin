package com.ruoyi.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraMove extends CameraDto{
    private double vertical; // 上下,范围-1 至 1
    private double horizontal; // 左右,范围-1 至 1
}
