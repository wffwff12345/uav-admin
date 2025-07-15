package com.ruoyi.uav.admin.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.JSONHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.uav.admin.NewWebSocketAppServer;
import com.ruoyi.uav.admin.NewWebSocketWebServer;
import com.ruoyi.uav.admin.service.INewWebSocketMsgService;
import com.ruoyi.websocket.domain.WebSocketVehicle;
import com.ruoyi.websocket.menu.SocketMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class NewWebSocketMsgServiceImpl implements INewWebSocketMsgService {

    @Resource
    private RedisCache redisCache;

    @Value("${websocket.cacheSize:120}")
    private long cacheSize;

//  private static AtomicInteger flag = new AtomicInteger(0);

    private static ConcurrentHashMap<String, List<WebSocketVehicle>> listPool = new ConcurrentHashMap<>();
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
                        vehicle.setVehicleLong(BigDecimal.valueOf(0));
                        vehicle.setVehicleLat(BigDecimal.valueOf(0));
                    }
                    sendMessage(vehicleId, message);
                    handleVehicleData(vehicleId, vehicle);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebSocketVehicle getCurrentVehicleInfo(String mac) {
        List<WebSocketVehicle> rightListData = redisCache.getCacheRightListData(mac);
        if (rightListData.size() == 0) {
            if (listPool.get(mac) != null && listPool.get(mac).size() > 0) {
                List<WebSocketVehicle> vehicles = listPool.get(mac);
                WebSocketVehicle ve = vehicles.get(vehicles.size() - 1);
                return ve;
            } else {
                return new WebSocketVehicle();
            }
        } else {
            return rightListData.get(0);
        }
    }

    private void handleVehicleData(String mac, WebSocketVehicle vehicle) {
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
    }

    public void postBathRedisToDB(String key, AtomicInteger atomicInteger) {
        /*List<WebSocketVehicle> list = redisCache.getCacheList(key);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Date date = new Date();
        List<WebSocketVehicle> beforeList = list.stream().filter(o -> o.getCreateTime().toInstant().isBefore(Instant.now())).collect(Collectors.toList());
        try {
            webSocketMsgMapper.saveBatch(beforeList);
            redisCache.getCacheListTrim(key, beforeList.size(), -1l);
            atomicInteger.set(0);
        } catch (Exception exception) {
            // 记录异常信息
            log.error("Error processing data: " + exception);
        }*/
    }

    public void sendMessage(String macId, String message) {
        try {
            // 检查macId是否为空或无效
            if (macId == null || macId.isEmpty()) {
                throw new IllegalArgumentException("MAC ID cannot be null or empty.");
            }
            NewWebSocketAppServer.sendMessageByMac(macId, message);
            NewWebSocketWebServer.sendMessageByMac(macId, message);
            /*WebSocketAppServer.sendAllMessage(message);
            WebSocketWebServer.sendAllMessage(message);*/
        } catch (IllegalArgumentException e) {
            log.error("Error sending message: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他异常
            log.error("An unexpected error occurred while sending the message: " + e.getMessage());
        }
    }

}
