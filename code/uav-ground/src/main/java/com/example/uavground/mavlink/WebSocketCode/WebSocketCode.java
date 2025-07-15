package com.example.uavground.mavlink.WebSocketCode;

import com.MAVLink.common.msg_command_long;
import com.alibaba.fastjson2.JSON;
import com.example.uavground.mavlink.Client;
import com.example.uavground.mavlink.dto.CommandDto;
import com.example.uavground.mavlink.dto.LatLngDto;
import com.example.uavground.mavlink.handler.WebSocketClientCallback;
import com.example.uavground.mavlink.handler.WebSocketClientCmdCallback;
import com.example.uavground.mavlink.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public enum WebSocketCode implements WebSocketClientCallback {
    /*CMD_TEST("cmdTest", "cmdTest"),
    CMD("cmd", "cmd"),
    LOCK("lock", "lock"),
    UNLOCK("unlock", "unlock"),
    MODE("mode", "mode"),
    LAND("land", "land"),
    TAKEOFF("takeOff", "takeOff"),
    TEST1561("test1561", "test1561"),
    TEST1571("test1571", "test1571"),
    TEST1581("test1581", "test1581"),
    SENDMSG("sendMsg", "sendMsg"),*/
    // 上锁
    UNLOCK() {
        @Override
        public void callback(String json) {
            Client.arm = 1;
            Client.lockFlag = 1;
        }
    },
    // 解锁与上锁
    LOCKORUNLOCK() {
        @Override
        public void callback(String json) {
            Client.arm = Integer.parseInt(json);
            Client.lockFlag = 1;
        }
    },
    // 设置模式
    MODE() {
        @Override
        public void callback(String json) {
            Client.uavMode = Short.parseShort(json);
            Client.modeFlag = 1;
            System.out.println("setMode" + Client.uavMode);
        }
    },
    // 一键起飞
    TAKEOFF() {
        @Override
        public void callback(String json) {
            Client.takeOffAltitude = Float.parseFloat(json);
            Client.safeTakeOffFlag = 1;
        }
    },
    // 降落
    LAND() {
        @Override
        public void callback(String json) {
            Client.landFlag = 1;
        }
    },
    // 一键返航
    LAUNCH() {
        @Override
        public void callback(String json) {
            Client.launchFlag = 1;
        }
    },
    // 添加航点
    ADDMISSION() {
        @Override
        public void callback(String json) {
            Client.wayPoints = JsonUtils.toList(json, new TypeReference<List<LatLngDto>>() {
            });
            System.out.println("addMission" + Client.wayPoints);
            Client.addMissionFlag = 1;
        }
    },
    // 开始航点飞行
    STARTMISSION() {
        @Override
        public void callback(String json) {
            Client.startMissionFlag = 1;
        }
    },
    // CMD测试命令
    COMMAND() {
        @Override
        public void callback(String json) {
            Client.cmdFlag = 1;
            System.out.println("command " + json);
            /*CommandDto cmd = JSON.parseObject(json.replace("NaN","-1"), CommandDto.class);
            com.MAVLink.common.msg_command_long command = new msg_command_long();
            command.param1 = Float.NaN;
            command.param2 = Float.NaN;
            System.out.println("command "+command);*/
            Client.setCmdCallback(new WebSocketClientCmdCallback() {
                @Override
                public void command(Socket socket, int sysId) {
                    try {
                        com.MAVLink.common.msg_command_long command = new msg_command_long();
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
                        Client.cmdFlag = 0;
                    }
                }
            });
        }
    },
    LOCK() {
        @Override
        public void callback(String json) {
            Client.lockFlag = 1;
            Client.arm = 1;
        }
    };

}
