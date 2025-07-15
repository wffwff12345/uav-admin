package com.ruoyi.uav.admin.service.Impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.uav.admin.WebSocketCode.WebSocketCode;
import com.ruoyi.uav.admin.service.INewWebSocketAppMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class NewWebSocketAppMsgServiceImpl implements INewWebSocketAppMsgService {

    @Autowired
    UavVehicleInfoServiceImpl uavVehicleInfoService;

    @Override
    public void handleMessage(String macId, String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        log.info("设备ID：" + macId + ",推送内容：" + message);
        if (UavVehicleInfoServiceImpl.taskMap == null) {
            log.error("taskMap 未初始化");
            return;
        }
        /*Vehicle vehicle = uavVehicleInfoService.selectUavVehicleInfoByVehicleId(Long.parseLong(macId));
        if (vehicle != null && vehicle.getVehicleStatus().equals("1")) {
            if (StringUtils.isNoneBlank(vehicle.getVehicleMac())) {
                String taskName = vehicle.getVehicleId() + "#" + vehicle.getVehicleMac();
                UavVehicleInfoServiceImpl.RunnableTask runnableTask = UavVehicleInfoServiceImpl.taskMap.get(taskName);
                if (runnableTask == null) {
                    log.warn("未找到对应的 RunnableTask, macId: {}", macId);
                    return;
                }
                WebSocketCode webSocketCode = WebSocketCode.valueOf(message.split("#")[0]);
                webSocketCode.callback(runnableTask, message.split("#")[1]);
            }
        }*/
    }
}
