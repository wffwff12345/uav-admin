package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.uav.admin.service.IUavVehicleTaskService;
import com.ruoyi.websocket.domain.TakeOff;
import com.ruoyi.websocket.domain.UavVehicleTask;
import com.ruoyi.websocket.service.IWebSocketAppMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("beforeExecuteTask")
public class BeforeExecuteTask {

    @Value("${task.beforeExecuteTask.time:120}")
    private String time;

    @Value("${task.beforeExecuteTask.defaultAlt:12}")
    private String defaultAlt;

    @Autowired
    private IWebSocketAppMsgService msgService;

    @Autowired
    private IUavVehicleTaskService taskService;

    public void startTask()
    {
        System.out.println("任务航线初始化操作");
        UavVehicleTask uavVehicleTask = new UavVehicleTask();
        uavVehicleTask.setVehicleTaskStatus("0");
        uavVehicleTask.setIsAvailable("0");
        try {
            List<UavVehicleTask> uavVehicleTasks = taskService.selectBeforeUavVehicleTaskList(uavVehicleTask);
            for (UavVehicleTask task : uavVehicleTasks) {
                TakeOff takeoff = new TakeOff(task.getSysId(), Double.parseDouble(defaultAlt));
                List list = new ArrayList<>();
                list.add(takeoff);
                String msg = "takeoff#" + JsonUtils.toJson(list);
                msgService.handleMessage(task.getSysId(), msg);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
