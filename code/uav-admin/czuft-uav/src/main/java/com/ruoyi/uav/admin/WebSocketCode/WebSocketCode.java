package com.ruoyi.uav.admin.WebSocketCode;

import com.MAVLink.common.msg_command_long;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.common.utils.JSONHelper;
import com.ruoyi.uav.admin.callback.WebSocketClientCallback;
import com.ruoyi.uav.admin.callback.WebSocketClientCmdCallback;
import com.ruoyi.uav.admin.domain.CommandDto;
import com.ruoyi.uav.admin.domain.LatLngDto;
import com.ruoyi.uav.admin.service.Impl.UavVehicleInfoServiceImpl;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public enum WebSocketCode implements WebSocketClientCallback {

    // 上锁
    UNLOCK() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask ,String json) {
            runnableTask.arm = 1;
            runnableTask.lockFlag = 1;
        }
    },
    // 解锁与上锁
    LOCKORUNLOCK() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json) {
            runnableTask.arm = Integer.parseInt(json);
            runnableTask.lockFlag = 1;
        }
    },
    // 设置模式
    MODE() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json) {
            runnableTask.uavMode = Short.parseShort(json);
            runnableTask.modeFlag = 1;
            System.out.println("setMode" + runnableTask.uavMode);
        }
    },
    // 一键起飞
    TAKEOFF() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask,String json) {
            runnableTask.takeOffAltitude = Float.parseFloat(json);
            runnableTask.safeTakeOffFlag = 1;
        }
    },
    // 降落
    LAND() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask,String json) {
            runnableTask.landFlag = 1;
        }
    },
    // 一键返航
    LAUNCH() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json) {
            runnableTask.launchFlag = 1;
        }
    },
    // 添加航点
    ADDMISSION() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask,String json) {
            try {
                runnableTask.wayPoints = JSONHelper.parse(json, new TypeReference<List<LatLngDto>>() {
                });
                System.out.println("addMission" + runnableTask.wayPoints);
                runnableTask.addMissionFlag = 1;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    },
    // 开始航点飞行
    STARTMISSION() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json) {
            runnableTask.startMissionFlag = 1;
        }
    },
    // CMD测试命令
    COMMAND() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask, String json) {
            runnableTask.cmdFlag = 1;
            /*CommandDto cmd = JSON.parseObject(json.replace("NaN","-1"), CommandDto.class);
            com.MAVLink.common.msg_command_long command = new msg_command_long();
            command.param1 = Float.NaN;
            command.param2 = Float.NaN;
            System.out.println("command "+command);*/
            runnableTask.setCmdCallback(new WebSocketClientCmdCallback() {
                @Override
                public void command(Socket socket, int sysId) {
                    try {
                        msg_command_long command = new msg_command_long();
                        CommandDto cmd;
                        if (json.contains("NaN")) {
                            cmd = JSON.parseObject(json.replace("NaN", "-1"), CommandDto.class);
                            command.param1 = Float.NaN;
                            command.param2 = Float.NaN;
                        } else {
                           cmd = JSON.parseObject(json, CommandDto.class);
                            command.param1 = cmd.getParam1();
                            command.param2 = cmd.getParam2();
                        }
                        // command.command = 21;
                        // 设置云台命令类型
                        command.sysid = sysId;
                        command.command = cmd.getCommand();
                        command.param3 = cmd.getParam3();
                        command.param4 = cmd.getParam4();
                        command.param5 = cmd.getParam5();
                        command.param6 = cmd.getParam6();
                        command.param7 = cmd.getParam7();
                        //command.confirmation = 0;
                        //command.param2 = 21196;
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(command.pack().encodePacket());
                        outputStream.flush();
                    } catch (IOException e) {
                        System.out.println("Send cmd error");
                        e.printStackTrace();
                    } finally {
                        runnableTask.cmdFlag = 0;
                    }
                }
            });
        }
    },
    LOCK() {
        @Override
        public void callback(UavVehicleInfoServiceImpl.RunnableTask runnableTask,String json) {
            runnableTask.lockFlag = 1;
            runnableTask.arm = 1;
        }
    };

}
