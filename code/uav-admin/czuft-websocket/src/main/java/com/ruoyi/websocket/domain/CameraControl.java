package com.ruoyi.websocket.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraControl {
    // 无人机id
    public int fid;
    //左右,范围-100至100
    public int horizontal;
    // 上下,范围-100至100
    public int vertical;
    //视场设置整数部分：0x1---0x1E
    public int zoomInt;
    //视场设置zoom微调部分：0---0x9
    public int zoomFloat;
}

