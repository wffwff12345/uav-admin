package com.example.uavground.mavlink.handler;


import java.net.Socket;

public interface WebSocketClientCmdCallback {
    public void command(Socket socket, int sysId);
}
