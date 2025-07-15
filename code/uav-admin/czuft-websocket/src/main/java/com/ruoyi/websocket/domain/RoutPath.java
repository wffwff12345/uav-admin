package com.ruoyi.websocket.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoutPath {
    // 无人机fid地址---指定的飞机id
    public int fid;
    //0-区域巡逻，1-路径巡逻
    public int type;
    //循环任务 ：1单次任务，0会一直执行（不建议使用）
    public int repeatCount;
    //航点数量
    public int wpCount;
    //最后一个动作 0: 返航 1：原地降落
    public int nextAction;
    //任务航点信息
    public List<com.example.uavapplication.model.PointLatLngAlt> points;
}
