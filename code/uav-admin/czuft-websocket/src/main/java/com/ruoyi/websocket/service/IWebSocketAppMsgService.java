package com.ruoyi.websocket.service;

public interface IWebSocketAppMsgService {
    public void handleMessage(String message);

    public void handleMessage(Integer sysId, String message);
}
