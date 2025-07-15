package com.ruoyi.websocket.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TabModel {
    // 无人机id
    public int fid;
    //0:电视，1：红外(热成像)
    public int mode;
    //当前模式下的热成像子模式
    public int subMode;
}
