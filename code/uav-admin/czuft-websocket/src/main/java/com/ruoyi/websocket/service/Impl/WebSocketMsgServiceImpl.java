package com.ruoyi.websocket.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.utils.JSONHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.websocket.domain.AckMsg;
import com.ruoyi.websocket.domain.UavVehicleTask;
import com.ruoyi.websocket.domain.VehicleInfo;
import com.ruoyi.websocket.mapper.WebUavVehicleInfoMapper;
import com.ruoyi.websocket.menu.SocketMethod;
import com.ruoyi.websocket.domain.WebSocketVehicle;
import com.ruoyi.websocket.service.IVehicleTaskService;
import com.ruoyi.websocket.service.IWebSocketMsgService;
import com.ruoyi.websocket.webSocket.NewWebSocketServer;
import com.ruoyi.websocket.webSocket.WebSocketAppServer;
import com.ruoyi.websocket.webSocket.WebSocketServer;
import com.ruoyi.websocket.webSocket.WebSocketWebServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WebSocketMsgServiceImpl implements IWebSocketMsgService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private WebUavVehicleInfoMapper webUavVehicleInfoMapper;

    @Resource
    private IVehicleTaskService taskService;

    @Value("${websocket.cacheSize:120}")
    private long cacheSize;

//  private static AtomicInteger flag = new AtomicInteger(0);

    /*private static ConcurrentHashMap<String, List<WebSocketVehicle>> listPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, AtomicInteger> flagPool = new ConcurrentHashMap<>();*/

    private static ConcurrentHashMap<String, List<VehicleInfo>> listPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, AtomicInteger> flagPool = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(String message) {
        System.out.println(message);
        // 增加边界条件检查
        String[] data = StringUtils.split(message, "#");
        if (data == null || data.length < 2) {
            log.warn("Invalid message format: {}", message);
            return;
        }
        if (StringUtils.equals(SocketMethod.VEHICLEINFO.value(), data[0])) {
            try {
                WebSocketVehicle vehicle = JSONHelper.parse(data[1], WebSocketVehicle.class);
                if (vehicle != null) {
                    vehicle.setCreateTime(new Date());
                    String vehicleId = vehicle.getVehicleId().toString();
                    if (vehicle.getVehicleLong() == null || vehicle.getVehicleLat() == null) {
                        return;
                    }
                    //saveSession(vehicle.getMac(), session);
                    //sendMessage(vehicleId, message);
                    //handleVehicleData(vehicleId, vehicle);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleMessage(String message, Session session) {
        try {
            VehicleInfo vehicle = JSONHelper.parse(message, VehicleInfo.class);
            sendMessage(vehicle.getSysId(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void QGCHandleMessage(String message) {
        // 增加边界条件检查
        String[] data = StringUtils.split(message, "#");
        if (data == null || data.length < 2) {
            log.warn("Invalid message format: {}", message);
            return;
        }
        if (StringUtils.equals(SocketMethod.VEHICLE.value(), data[0])) {
            try {
                VehicleInfo vehicleInfo = JSONHelper.parse(data[1], VehicleInfo.class);
                int sysId = vehicleInfo.getSysId();
                sendMessage(sysId, message);
                if (vehicleInfo.getLng() == 0.0 || vehicleInfo.getLat() == 0.0) {
                    //return;
                }
                handleVehicleData(sysId, vehicleInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (StringUtils.equals(SocketMethod.ASKMSG.value(), data[0])) {
            try {
                AckMsg askMsg = JSONHelper.parse(data[1], AckMsg.class);
                if (askMsg.getCodeId() == 2 && askMsg.getCodeStatu() == 0) {
                    UavVehicleTask uavVehicleTask = new UavVehicleTask();
                    uavVehicleTask.setSysId(askMsg.getFid());
                    uavVehicleTask.setVehicleTaskStatus("0");
                    uavVehicleTask.setIsAvailable("0");
                    try {
                        List<UavVehicleTask> uavVehicleTasks = taskService.selectUavVehicleTaskList(uavVehicleTask);
                        if (uavVehicleTasks != null && uavVehicleTasks.size() > 0) {
                            uavVehicleTasks.get(0).setIsAvailable(("1"));
                            taskService.updateUavVehicleTask(uavVehicleTasks.get(0));
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebSocketVehicle getCurrentVehicleInfo(String mac) {
        /*List<WebSocketVehicle> rightListData = redisCache.getCacheRightListData(mac);
        if (rightListData.size() == 0) {
            if (listPool.get(mac) != null && listPool.get(mac).size() > 0) {
                List<Vehicle> vehicles = listPool.get(mac);
                WebSocketVehicle ve = vehicles.get(vehicles.size() - 1);
                return ve;
            } else {
                return new WebSocketVehicle();
            }
        } else {
            return rightListData.get(0);
        }*/
        return new WebSocketVehicle();
    }

    /*private void handleVehicleData(String mac, WebSocketVehicle vehicle) {
        Long size = redisCache.getCacheListSize(mac);
        if (size == 0 && listPool.get(mac) != null && !listPool.get(mac).isEmpty()) {
            long l = redisCache.setCacheList(mac, listPool.get(mac));
            listPool.remove(mac);
        }
        if (size < cacheSize) {
            long l = redisCache.setCacheListData(mac, vehicle);
        } else {
            AtomicInteger atomicInteger = flagPool.computeIfAbsent(mac, k -> new AtomicInteger(0));
            if (0 == atomicInteger.getAndIncrement()) {
                List<WebSocketVehicle> vehicles = new ArrayList<>();
                vehicles.add(vehicle);
                listPool.put(mac, vehicles);
                postBathRedisToDB(mac, atomicInteger);
            } else {
                listPool.computeIfPresent(mac, (k, v) -> {
                    v.add(vehicle);
                    return v;
                });
            }
        }
    }*/

    private void handleVehicleData(int sysId, VehicleInfo vehicleInfo) {
        vehicleInfo.setCreateTime(new Date());
        String sysIdStr = sysId + "";
        Long size = redisCache.getCacheListSize(sysIdStr);
        if (size == 0 && listPool.get(sysIdStr) != null && !listPool.get(sysIdStr).isEmpty()) {
            long l = redisCache.setCacheList(sysIdStr, listPool.get(sysIdStr));
            listPool.remove(sysIdStr);
        }
        if (size < cacheSize) {
            long l = redisCache.setCacheListData(sysIdStr, vehicleInfo);
        } else {
            AtomicInteger atomicInteger = flagPool.computeIfAbsent(sysIdStr, k -> new AtomicInteger(0));
            if (0 == atomicInteger.getAndIncrement()) {
                List<VehicleInfo> vehicleInfos = new ArrayList<>();
                vehicleInfos.add(vehicleInfo);
                listPool.put(sysIdStr, vehicleInfos);
                postBathRedisToDB(sysIdStr, atomicInteger);
            } else {
                listPool.computeIfPresent(sysIdStr, (k, v) -> {
                    v.add(vehicleInfo);
                    return v;
                });
            }
        }
    }

    public void postBathRedisToDB(String key, AtomicInteger atomicInteger) {
        List<VehicleInfo> list = redisCache.getCacheList(key);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<VehicleInfo> beforeList = list.stream().filter(o -> o.getCreateTime().toInstant().isBefore(Instant.now())).collect(Collectors.toList());
        try {
            webUavVehicleInfoMapper.saveBatch(beforeList);
            redisCache.getCacheListTrim(key, beforeList.size(), -1l);
            atomicInteger.set(0);
        } catch (Exception exception) {
            // 记录异常信息
            log.error("Error processing data: " + exception);
        }
    }

    public void sendMessage(int sysId, String message) {
        try {
            if (sysId == 0) {
                return;
            }
            message = "vehicle#" + message;
            WebSocketAppServer.sendMessageByMac(sysId, message);
            WebSocketWebServer.sendMessageByMac(sysId, message);
            NewWebSocketServer.sendAllMessage(message);
        } catch (IllegalArgumentException e) {
            log.error("Error sending message: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他异常
            log.error("An unexpected error occurred while sending the message: " + e.getMessage());
        }
    }

    public void saveSession(String macId, Session session) {
        try {
            WebSocketServer.sessionPool.putIfAbsent(macId, session);
        } catch (Exception e) {
            log.error("socket 重复连接异常,错误信息：" + e.getMessage(), e);
        }
    }
}
