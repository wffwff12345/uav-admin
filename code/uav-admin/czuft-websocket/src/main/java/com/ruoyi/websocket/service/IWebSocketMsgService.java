package com.ruoyi.websocket.service;

import com.ruoyi.websocket.domain.WebSocketVehicle;

import javax.websocket.Session;


public interface IWebSocketMsgService {
    public void handleMessage(String message);
    public void handleMessage(String message, Session session);
    public WebSocketVehicle getCurrentVehicleInfo(String mac);
    public void QGCHandleMessage(String message);

}
