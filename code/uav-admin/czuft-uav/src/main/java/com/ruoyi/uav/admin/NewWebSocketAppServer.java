package com.ruoyi.uav.admin;

import com.ruoyi.uav.admin.service.INewWebSocketAppMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/new/app/{macId}")
public class NewWebSocketAppServer {

    public static INewWebSocketAppMsgService msgService;

    @Autowired
    public void setMsgService(INewWebSocketAppMsgService msgService) {
        this.msgService = msgService;
    }

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    public String macId;

    // session集合,存放对应的session
    public static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    public static CopyOnWriteArraySet<NewWebSocketAppServer> webSocketSet = new CopyOnWriteArraySet<>();


    /**
     * 建立WebSocket连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("macId") String macId) {
        log.info("App WebSocket建立连接中,连接设备macId：{}", macId);
        try {
            Session historySession = sessionPool.get(macId);
            // historySession不为空,说明已经有设备连接,应该删除连接的WebSocket对象
            if (Objects.nonNull(historySession)) {
                log.info("删除登陆的WebSocket对象", macId);
                //webSocketSet.remove(historySession);
                sessionPool.remove(macId);
                historySession.close();
            }
        } catch (Exception e) {
            log.error("重复连接异常,错误信息：" + e.getMessage(), e);
        }
        this.session = session;
        this.macId = macId;
        webSocketSet.add(this);
        sessionPool.put(macId, session);
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
        log.error("App WebSocket连接发生报错" + throwable.toString());
        synchronized (this) {
            webSocketSet.remove(this);
        }
        // 使用线程安全的Map，无需额外同步
        sessionPool.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        sessionPool.remove(this.macId);
        log.info("App WebSocket连接断开,当前在线设备数为：{}", webSocketSet.size());
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
        if (message.startsWith("heartbeat", 4)) {
            return;
        }
        System.out.println("收到来自设备App" + this.macId + "的消息，设备地址：" + session.getMessageHandlers() + "，消息内容：" + message);
        msgService.handleMessage(this.macId, message);
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
            if (Objects.nonNull(session) && session.isOpen()) {
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
}
