package com.example.uavground.mavlink.controller;

import com.example.uavground.mavlink.Client;
import com.example.uavground.mavlink.dto.CommandDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uav")
public class UavController {

    // 解锁1-- 0: disarm, 1: arm 你可以使无人机进入准备飞行状态（arming）或关闭电机（disarming）
    @GetMapping("/unlock")
    public String lock(int arm) {
        Client.lockFlag = 1;
        Client.arm = arm;
        return "解锁";
    }

    // 上锁 0
    @GetMapping("/lock")
    public String unLock(int arm) {
        Client.lockFlag = 2;
        Client.arm = arm;
        return "上锁";
    }

    // 设置模式
    @GetMapping("/mode/{modeId}")
    public String mode(@PathVariable("modeId") short modeId) {
        Client.modeFlag = 1;
        Client.uavMode = modeId;
        return "设置模式";
    }

    // 起飞
    @GetMapping("/takeOff")
    public String takeOff() {
        Client.takeOffFlag = 1;
        return "再次确认起飞！";
    }

    // 双重确认起飞
    @GetMapping("/safe/takeOff")
    public String safeTakeOff(String altitude) {
        Client.safeTakeOffFlag = 1;
        Client.takeOffAltitude = Float.parseFloat(altitude);
        return "双重确认起飞";
    }

    // 降落
    @GetMapping("/land")
    public String onLand() {
        Client.landFlag = 1;
        return "降落";
    }

    @GetMapping("/launch")
    public String launch() {
        Client.launchFlag = 1;
        return "launch!";
    }

    @GetMapping("/addMission")
    public String addMission() {
        /*Client.missionItemX = (int) (Double.parseDouble(json.split(",")[0]) * 10000000);
        Client.missionItemY = (int) (Double.parseDouble(json.split(",")[1]) * 10000000);
        System.out.println("addMission" + Client.missionItemX + " " + Client.missionItemY);
        Client.missionItemZ = Float.parseFloat("10");*/
        Client.addMissionFlag = 1;
        return "addMissionFlag!";
    }
    @GetMapping("/startMission")
    public String startMission() {
        Client.startMissionFlag = 1;
        return "startMission";
    }

    @GetMapping("/test1561")
    public String test1561(short stab_pitch, short stab_yaw, short mountMode) {
        Client.test1561Flag = 1;
        Client.mount_mode = mountMode;
        Client.stab_pitch = stab_pitch;
        Client.stab_yaw = stab_yaw;
        return "test1561!";
    }

    @GetMapping("/test1571")
    public String test1571(int input_a, int input_b) {
        Client.test1571Flag = 1;
        Client.input_a = input_a;
        Client.input_b = input_b;
        return "test1571!";
    }

    @GetMapping("/test1581")
    public String test1581(int poninting_a, int pointing_b, short mountMode) {
        Client.mount_mode = mountMode;
        Client.test1581Flag = 1;
        Client.pointing_a = poninting_a;
        Client.pointing_b = pointing_b;
        Client.pointing_c = 1;
        return "test1581!";
    }

    @GetMapping("/cancel/test1581")
    public String cancelTest1581(int poninting_a, int pointing_b) {
        Client.test1581Flag = 1;
        Client.pointing_a = poninting_a;
        Client.pointing_b = pointing_b;
        Client.pointing_c = 0;
        return "cancelTest1581!";
    }

    @GetMapping("/sendMsg")
    public String sendMsg() {
        return "sendMsg!";
    }

    @PostMapping("/cmd")
    public String cmd(@RequestBody CommandDto cmd) {
        Client.cmdFlag = 1;
        Client.cmd = cmd;
        return "cmd send!";
    }

    @PostMapping("/cmdTest")
    public String cmdTest(@RequestBody CommandDto cmd) {
        Client.cmdFlag = 1;
        Client.cmd = cmd;
        Client.cmdTest(Client.cmd);
        return "cmdTest send!";
    }

    @GetMapping("/openWebSocket")
    public String openWebSocket() {
        return "openWebSocket!";
    }
}
