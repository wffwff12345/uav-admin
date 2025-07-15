package com.ruoyi.uav.admin.service;


import com.ruoyi.websocket.domain.WebSocketVehicle;

public interface INewWebSocketMsgService {
    public void handleMessage(String message);
    public WebSocketVehicle getCurrentVehicleInfo(String mac);
}
