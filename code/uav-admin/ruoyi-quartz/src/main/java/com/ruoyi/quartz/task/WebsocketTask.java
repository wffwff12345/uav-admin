package com.ruoyi.quartz.task;

import com.ruoyi.websocket.webSocket.WebSocketServer;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("websocketTask")
public class WebsocketTask {
    /**
     * 群发消息
     *
     * @param mac
     */
    public static void sendAllMessage(String mac) {
        WebSocketServer.sendAllMessage(mac);
    }
}
