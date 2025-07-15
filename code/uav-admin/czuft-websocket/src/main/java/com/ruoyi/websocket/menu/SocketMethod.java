package com.ruoyi.websocket.menu;

public enum SocketMethod {
    ROUTEPATH("routPath"),// 航线飞行任务
    UAVMOVE("uavMove"),// 单点飞行
    TAKEOFF("takeoff"),// 起飞
    RTL("Rtl"),// 返航
    LOITER("loiter"),// 悬停
    ASKMSG("ackMsg"),// 确认消息
    VEHICLE("vehicle"),// 无人机信息
    CAMERACONTROL("cameraControl"),//云平台控制

    VEHICLEINFO("vehicleInfo"),
    CAMERAMOVE("cameraMove"), // 云平台转向
    CAMERAFOLLOW("cameraFollow"); // 云平台跟踪;

    private final String value;

    SocketMethod(String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }

}
