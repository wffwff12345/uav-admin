package com.ruoyi.websocket.webSocket;

import com.ruoyi.websocket.service.IWebSocketMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/web")
public class NewWebSocketServer {
    public static IWebSocketMsgService msgService;

    @Autowired
    public void setMsgService(IWebSocketMsgService msgService) {
        this.msgService = msgService;
    }

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    public String macId;

    // session集合,存放对应的session
    public static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    public static CopyOnWriteArraySet<NewWebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();


    /**
     * 建立WebSocket连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("Web WebSocket建立连接中");
        this.session = session;
        webSocketSet.add(this);
        log.info("建立连接完成,当前在线设备数为：{}", webSocketSet.size());
    }

    /**
     * 连接发生报错时执行
     *
     * @param session   会话
     * @param throwable 报错
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Web 连接发生报错 " + throwable.toString());
        synchronized (this) {
            webSocketSet.remove(this);
        }
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        synchronized (this) {
            webSocketSet.remove(this);
        }
        // 使用线程安全的Map，无需额外同步
        //sessionPool.entrySet().removeIf(entry -> entry.getValue().equals(session));
        log.info("Web 连接断开,当前在线设备数为：{}", webSocketSet.size());
    }

    /**
     * 收到客户端的消息时执行
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自设备的消息，设备地址：{}，消息内容：{}", session.getMessageHandlers(), message);
        // 业务逻辑，对消息的处理
        // sendMessageToAll("群发消息的内容");
        msgService.handleMessage(message);
    }

    /**
     * 向客户端推送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
        log.info("推送消息给设备:{}，消息内容为：{}", this.session.getMessageHandlers(), message);
    }

    /**
     * 推送消息到指定设备
     *
     * @param macId   设备ID
     * @param message 发送的消息
     */
    public static void sendMessageByMac(String macId, String message) {
        log.info("设备ID：" + macId + ",推送内容：" + message);
        try {
            Session session = sessionPool.get(macId);
            if (Objects.nonNull(session)) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("推送消息到指定设备发生错误：" + e.getMessage(), e);
        }
    }

    /**
     * 群发消息
     *
     * @param message
     */
    public static void sendAllMessage(String message) {
        log.info("发送消息：{}", message);
        webSocketSet.forEach(webSocket -> {
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("群发消息发生错误：" + e.getMessage(), e);
            }
        });
    }

    public  void updateVehicleStatus(String macId, String vehicleStatus){
       /* Vehicle vehicle = new Vehicle();
        vehicle.setSysId(Long.parseLong(macId));
        List<Vehicle> uavVehicleList= uavVehicleInfoService.selectUavVehicleInfoList(vehicle);
        if(Objects.nonNull(uavVehicleList) && uavVehicleList.size() == 1){
            Vehicle vehicleInfo = uavVehicleList.get(0);
            vehicleInfo.setVehicleStatus(vehicleStatus);
            uavVehicleInfoService.updateUavVehicleInfo(vehicleInfo);
        }*/
    }
}
