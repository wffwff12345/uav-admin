package com.ruoyi.websocket.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AckMsg {
    //飞机id；
    public int fid;
    //0: routPath反馈；1：uavMove反馈；2：takeoff
    //3：Rtl反馈；4：loiter反馈
    public int codeId;
    //0:服务器指令或任务执行成功；-1：失败
    public int codeStatu;
    //指令或任务失败以后的反馈信息，没有错误不反馈信息
    public String codeMsg;
}
