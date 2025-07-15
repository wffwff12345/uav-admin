package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.uav.admin.domain.UavVehicleTaskItem;
import com.ruoyi.uav.admin.service.IUavVehicleService;
import com.ruoyi.uav.admin.service.IUavVehicleTaskItemService;
import com.ruoyi.uav.admin.service.IUavVehicleTaskService;
import com.ruoyi.websocket.domain.RoutPath;
import com.ruoyi.websocket.domain.UavVehicleTask;
import com.ruoyi.websocket.domain.Vehicle;
import com.ruoyi.websocket.service.IWebSocketAppMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.uavapplication.model.PointLatLngAlt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("vehicleTask")
public class VehicleTask {
    @Autowired
    private IUavVehicleTaskService taskService;
    @Autowired
    private IUavVehicleService uavVehicleService;
    @Autowired
    private IWebSocketAppMsgService msgService;
    @Autowired
    private IUavVehicleTaskItemService uavVehicleTaskItemService;

    public void startTask() {
        UavVehicleTask uavVehicleTask = new UavVehicleTask();
        uavVehicleTask.setVehicleTaskStatus("0");
        uavVehicleTask.setIsAvailable("1");
        uavVehicleTask.setVehicleTaskDatetime(new Date());
        List<UavVehicleTask> uavVehicleTasks = null;
        try {
            uavVehicleTasks = taskService.selectUavVehicleTaskListByStatus(uavVehicleTask);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (UavVehicleTask task : uavVehicleTasks) {
            task.setVehicleTaskStatus("1");
            taskService.updateUavVehicleTask(task);
            Vehicle vehicle = new Vehicle();
            vehicle.setSysId(Long.valueOf(task.getSysId()));
            vehicle.setVehicleStatus("1");
            List<Vehicle> vehicleList = uavVehicleService.selectUavVehicleList(vehicle);
            if (vehicleList.size() > 0) {
                UavVehicleTaskItem uavVehicleTaskItem = new UavVehicleTaskItem();
                uavVehicleTaskItem.setVehicleTaskId(task.getVehicleTaskId());
                List<UavVehicleTaskItem> uavVehicleTaskItems = uavVehicleTaskItemService.selectUavVehicleTaskItemList(uavVehicleTaskItem);
                if (uavVehicleTaskItems.size() > 0) {
                    List<PointLatLngAlt> list = uavVehicleTaskItems.stream().map(item -> {
                        String vehicleTaskItem = item.getVehicleTaskItem();
                        PointLatLngAlt parse = JsonUtils.toBean(vehicleTaskItem, PointLatLngAlt.class);
                        return parse;
                    }).collect(Collectors.toList());
                    RoutPath routPath = new RoutPath(task.getSysId(), 1, 1, list.size(),task.getNextAction(), list);
                    List<RoutPath> routPathList = new ArrayList<>();
                    routPathList.add(routPath);
                    String message = JsonUtils.toJson(routPathList);
//                    msgService.handleMessage(task.getVehicleSysId().toString(), "MODE" + "#" + 3);
                    msgService.handleMessage(task.getSysId(), "routePath#" + message);
                }
            }
        }
    }
}
