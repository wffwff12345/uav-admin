package com.ruoyi.websocket.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.utils.JSONHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.websocket.dto.CameraControl;
import com.ruoyi.websocket.dto.CameraDto;
import com.ruoyi.websocket.dto.CameraFollow;
import com.ruoyi.websocket.dto.CameraMove;
import com.ruoyi.websocket.menu.SocketMethod;
import com.ruoyi.websocket.service.IWebSocketAppMsgService;
import com.ruoyi.websocket.webSocket.QGCWebSocketServer;
import com.ruoyi.websocket.webSocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebSocketAppMsgServiceImpl implements IWebSocketAppMsgService {

    @Override
    public void handleMessage(String message) {
        // 增加边界条件检查
        String[] data = StringUtils.split(message, "#");
        if (data == null || data.length < 2) {
            log.warn("Invalid message format: {}", message);
            return;
        }
        try {
            if (StringUtils.equals(SocketMethod.CAMERAMOVE.value(), data[0])) {
                handleCameraMessage(CameraMove.class, data, message);
            } else if (StringUtils.equals(SocketMethod.CAMERAFOLLOW.value(), data[0])) {
                handleCameraMessage(CameraFollow.class, data, message);
            } else if (StringUtils.equals(SocketMethod.CAMERACONTROL.value(), data[0])) {
                handleCameraMessage(CameraControl.class, data, message);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Integer sysId,String message) {
        /*TakeOff takeoff = new TakeOff(101,11.00);
        List<TakeOff> list = new ArrayList<>();
        String  msg = "";
        try {
            list.add(takeoff);
            msg = JSONHelper.stringify(list);
            msg = "takeoff#"+msg;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        QGCWebSocketServer.sendAllMessage(message);
    }

    private void handleCameraMessage(Class<?> clazz, String[] data, String message) throws JsonProcessingException {
        Object cameraObject = JSONHelper.parse(data[1], clazz);
        if (cameraObject instanceof CameraDto) {
            CameraDto cameraDto = (CameraDto) cameraObject;
            if (StringUtils.isNoneBlank(cameraDto.getMac())) {
                WebSocketServer.sendMessageByMac(cameraDto.getMac(),message);
            }
        }
    }
   /* @PostConstruct
    public void init() {
        log.info("WebSocketAppMsgServiceImpl init");
        CameraDto cameraDto = new CameraDto("1");
    }*/
}
