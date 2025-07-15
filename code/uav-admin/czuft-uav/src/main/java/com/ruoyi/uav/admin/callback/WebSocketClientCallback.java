package com.ruoyi.uav.admin.callback;


import com.ruoyi.uav.admin.service.Impl.UavVehicleInfoServiceImpl;

public interface WebSocketClientCallback {
    void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json);
}
