package com.ruoyi.websocket.schedule;

import com.ruoyi.websocket.webSocket.WebSocketServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WebSocketSchedule {

   /* @Scheduled(cron = "0/1 * * * * ?")
    public void sendAllMessage() {
        WebSocketServer.sendAllMessage("群发消息" + UUID.randomUUID().toString());
    }*/
}
