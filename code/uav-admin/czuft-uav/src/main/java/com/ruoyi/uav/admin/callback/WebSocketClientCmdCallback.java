package com.ruoyi.uav.admin.callback;


import java.net.Socket;

public interface WebSocketClientCmdCallback {
    public void command(Socket socket, int sysId);
}
