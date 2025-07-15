package com.ruoyi.uav.admin.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.ardupilotmega.msg_ahrs;
import com.MAVLink.ardupilotmega.msg_ekf_status_report;
import com.MAVLink.ardupilotmega.msg_meminfo;
import com.MAVLink.ardupilotmega.msg_rpm;
import com.MAVLink.common.*;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_FRAME;
import com.MAVLink.minimal.msg_heartbeat;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JSONHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.uav.admin.callback.WebSocketClientCmdCallback;
import com.ruoyi.uav.admin.domain.CommandDto;
import com.ruoyi.uav.admin.domain.LatLngDto;
import com.ruoyi.uav.admin.domain.UavVehicleDto;
import com.ruoyi.uav.admin.service.INewWebSocketMsgService;
import com.ruoyi.websocket.domain.Vehicle;
import com.ruoyi.websocket.domain.VehicleInfo;
import com.ruoyi.websocket.menu.SocketMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.uav.admin.mapper.UavVehicleInfoMapper;
import com.ruoyi.uav.admin.service.IUavVehicleInfoService;

import static com.MAVLink.enums.MAV_CMD.MAV_CMD_NAV_WAYPOINT;

/**
 * 无人机基础信息管理Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-03
 */
@Slf4j
@Service
public class UavVehicleInfoServiceImpl implements IUavVehicleInfoService {
    // 创建线程池
    private static volatile ExecutorService executor = Executors.newFixedThreadPool(5);

    // 用于存储任务名称和对应的Future对象
    public static ConcurrentHashMap<String, Future<?>> FutureMap = new ConcurrentHashMap<>();

    // 用于存储任务名称和对应的RunnableTask对象
    public static ConcurrentHashMap<String, RunnableTask> taskMap = new ConcurrentHashMap<>();

    // 用于存储任务名称和对应的UavVehicleInfo对象
    public static ConcurrentHashMap<String, Vehicle> UavVehicleInfoMap = new ConcurrentHashMap<>();

    @Autowired
    private UavVehicleInfoMapper uavVehicleInfoMapper;

    public static INewWebSocketMsgService msgService;

    @Autowired
    public void setMsgService(INewWebSocketMsgService msgService) {
        this.msgService = msgService;
    }

    /**
     * 查询无人机基础信息管理
     *
     * @param vehicleId 无人机基础信息管理主键
     * @return 无人机基础信息管理
     */
    @Override
    public VehicleInfo selectUavVehicleInfoByVehicleId(Long vehicleId) {
        return uavVehicleInfoMapper.selectUavVehicleInfoByVehicleId(vehicleId);
    }

    /**
     * 查询无人机基础信息管理列表
     *
     * @param vehicleInfo 无人机基础信息管理
     * @return 无人机基础信息管理
     */
    @Override
    public List<VehicleInfo> selectUavVehicleInfoList(VehicleInfo vehicleInfo) {
        return uavVehicleInfoMapper.selectUavVehicleInfoList(vehicleInfo);
    }

    /**
     * 新增无人机基础信息管理
     *
     * @param vehicleInfo 无人机基础信息管理
     * @return 结果
     */
    @Override
    public int insertUavVehicleInfo(VehicleInfo vehicleInfo) {
        vehicleInfo.setCreateTime(DateUtils.getNowDate());
        return uavVehicleInfoMapper.insertUavVehicleInfo(vehicleInfo);
    }

    /**
     * 修改无人机基础信息管理
     *
     * @param vehicleInfo 无人机基础信息管理
     * @return 结果
     */
    @Override
    public int updateUavVehicleInfo(VehicleInfo vehicleInfo) {
        vehicleInfo.setUpdateTime(DateUtils.getNowDate());
        return uavVehicleInfoMapper.updateUavVehicleInfo(vehicleInfo);
    }

    /**
     * 批量删除无人机基础信息管理
     *
     * @param vehicleIds 需要删除的无人机基础信息管理主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleInfoByVehicleIds(Long[] vehicleIds) {
        return uavVehicleInfoMapper.deleteUavVehicleInfoByVehicleIds(vehicleIds);
    }

    /**
     * 删除无人机基础信息管理信息
     *
     * @param vehicleId 无人机基础信息管理主键
     * @return 结果
     */
    @Override
    public int deleteUavVehicleInfoByVehicleId(Long vehicleId) {
        return uavVehicleInfoMapper.deleteUavVehicleInfoByVehicleId(vehicleId);
    }

    // 启动连接
    /*@Override
    public int startConnectVehicle(Long vehicleId) {
        try {
            Vehicle vehicle = selectUavVehicleInfoByVehicleId(vehicleId);
            if (vehicle != null && vehicle.getVehicleStatus().equals("0")) {
                if (StringUtils.isNoneBlank(vehicle.getVehicleMac())) {
                    String taskName = vehicle.getVehicleId() + "#" + vehicle.getVehicleMac();
                    RunnableTask runnableTask = new RunnableTask(taskName);
                    taskMap.put(taskName, runnableTask);
                    FutureMap.put(taskName, executor.submit(runnableTask));
                }
                vehicle.setVehicleStatus("1");
                updateUavVehicleInfo(vehicle);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 1;
    }*/

    // 停止连接
    /*@Override
    public int stopConnectVehicle(Long vehicleId) {
        try {
            Vehicle vehicle = selectUavVehicleInfoByVehicleId(vehicleId);
            if (vehicle != null && vehicle.getVehicleStatus().equals("1")) {
                if (StringUtils.isNoneBlank(vehicle.getVehicleMac())) {
                    String taskName = vehicle.getVehicleId() + "#" + vehicle.getVehicleMac();
                    cancelTaskByName(FutureMap, taskName);
                }
                vehicle.setVehicleStatus("0");
                updateUavVehicleInfo(vehicle);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 1;
    }*/

    public static class RunnableTask implements Runnable {
        private final String name;

        private int sysId = 0;

        public int lockFlag = 0;

        public int sendMsgFlag = 0;

        public int arm = 0;

        public int modeFlag = 0;

        public short uavMode = 0;

        public int takeOffFlag = 1;

        public int safeTakeOffFlag = 0;

        public float takeOffAltitude = 0.0f;

        public int landFlag = 0;

        public short mount_mode = 2;

        public int test1561Flag = 0;

        public short stab_pitch = 0;

        public short stab_yaw = 0;

        public int test1571Flag = 0;

        public int input_a = 0;

        public int input_b = 0;

        public int test1581Flag = 0;

        public int pointing_a = 0;

        public int pointing_b = 0;

        public int pointing_c = 0;

        public volatile int cmdFlag = 0;

        public CommandDto cmd;

        public int launchFlag = 0;

        public int startMissionFlag = 0;

        public int addMissionFlag = 0;

        public int missionItemX = 0;

        public int missionItemY = 0;

        public float missionItemZ = 0;

        private AtomicInteger seq = new AtomicInteger(0);

        private UavVehicleDto uavVehicleDto = new UavVehicleDto();

        public WebSocketClientCmdCallback cmdCallback;

        public RunnableTask(String name) {
            this.name = name;
        }

        public void setCmdCallback(WebSocketClientCmdCallback callback) {
            cmdCallback = callback;
        }

        public volatile List<LatLngDto> wayPoints = new ArrayList<>();

        // 加、解锁 加锁：0 解锁：1
        public void lockOrUnLock(Socket socket, int arm) {
            try {
                System.out.println("lockOrUnLock " + arm);
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                // command.command = 21;
                // 设置云台命令类型
                command.sysid = sysId == 0 ? 1 : sysId;
                command.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
                command.param1 = arm;
                //command.confirmation = 0;
                //command.param2 = 21196;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send MAV_CMD_COMPONENT_ARM_DISARM error");
                e.printStackTrace();
            } finally {
                lockFlag = 0;
            }
        }

        // 设置模式
        public void setMode(Socket socket, short mode) {
            try {
                System.out.println("setMode");
                msg_set_mode msg_set_mode = new msg_set_mode();
                msg_set_mode.sysid = sysId;
                msg_set_mode.base_mode = 1;
                msg_set_mode.custom_mode = mode;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg_set_mode.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send MAVLINK_MSG_ID_MOUNT_STATUS error");
                e.printStackTrace();
            } finally {
                modeFlag = 0;
            }
        }

        // 起飞指令
        public void takeOff(Socket socket, float altitude) {
            try {
                System.out.println("takeOff:    " + altitude);
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                // command.command = 21;
                // 设置云台命令类型
                command.sysid = sysId;
                command.command = MAV_CMD.MAV_CMD_NAV_TAKEOFF;
                command.param7 = altitude;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send MAV_CMD_NAV_TAKEOFF error");
                e.printStackTrace();
            } finally {
                safeTakeOffFlag = 0;
            }
        }

        // 一键降落
        public void onLand(Socket socket) {
            try {
                System.out.println("onLand");
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                // command.command = 21;
                // 设置云台命令类型
                command.command = MAV_CMD.MAV_CMD_NAV_LAND;
                command.sysid = sysId;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send MAV_CMD_NAV_LAND error");
                e.printStackTrace();
            } finally {
                landFlag = 0;
            }
        }

        // 一键返航
        public void onLaunch(Socket socket) {
            try {
                System.out.println("onLaunch");
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                command.command = MAV_CMD.MAV_CMD_NAV_RETURN_TO_LAUNCH;
                command.sysid = sysId;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send MAV_CMD_NAV_RETURN_TO_LAUNCH error");
                e.printStackTrace();
            } finally {
                launchFlag = 0;
            }
        }

        public void addMissions(Socket socket) {
            try {
                if (addMissionFlag == 1) {
                    // 发送 MISSION_COUNT 消息
                    msg_mission_count missionCount = new msg_mission_count();
                    missionCount.sysid = sysId;
                    missionCount.count = wayPoints.size();
                    missionCount.pack().encodePacket();
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(missionCount.pack().encodePacket());
                    outputStream.flush();
                    for (int i = 0; i < wayPoints.size(); i++) {
                        addMission(socket, wayPoints.get(i));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 添加任务项
        public void addMission(Socket socket, LatLngDto latLngDto) {
            try {
                System.out.println("addMission");
                msg_mission_item_int missionItem = new msg_mission_item_int();
                missionItem.sysid = sysId;
                missionItem.seq = seq.getAndIncrement();
                missionItem.frame = MAV_FRAME.MAV_FRAME_GLOBAL_RELATIVE_ALT_INT;
                missionItem.command = MAV_CMD_NAV_WAYPOINT;
                missionItem.current = 0;
                missionItem.autocontinue = 1;
                missionItem.param1 = 5.0f;
                missionItem.param2 = 0.0f;
                missionItem.param3 = 1.0f;
                missionItem.param4 = 0.0f;
                missionItem.x = (int) (latLngDto.getLat() * 1e7);
                missionItem.y = (int) (latLngDto.getLng() * 1e7);
                missionItem.z = 10;
                missionItem.mission_type = 0;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(missionItem.pack().encodePacket());
                outputStream.flush();
                //testFlag++;
                //System.out.println("addMissionFlag: " + testFlag);
            } catch (IOException e) {
                System.out.println("Send addMission error");
                e.printStackTrace();
            } finally {
                addMissionFlag = 0;
            }
        }

        // 启动任务
        public void startMission(Socket socket) {
            try {
                System.out.println("startMission");
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                command.command = MAV_CMD.MAV_CMD_MISSION_START;
                command.sysid = sysId;
                command.param1 = 0;
                command.param2 = 0;
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send startMission error");
                e.printStackTrace();
            } finally {
                startMissionFlag = 0;
                seq.set(0);
            }
        }

        //
        public void cmd(Socket socket, CommandDto cmd) {
            try {
                System.out.println("cmd " + arm);
                com.MAVLink.common.msg_command_long command = new msg_command_long();
                // command.command = 21;
                // 设置云台命令类型
                command.sysid = cmd.getSysId() == 0 ? 1 : sysId;
                command.command = cmd.getCommand();
                command.param1 = cmd.getParam1();
                command.param2 = cmd.getParam2();
                command.param3 = cmd.getParam3();
                command.param4 = cmd.getParam4();
                command.param5 = cmd.getParam5();
                command.param6 = cmd.getParam6();
                command.param7 = cmd.getParam7();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.pack().encodePacket());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Send cmd error");
                e.printStackTrace();
            } finally {
                cmdFlag = 0;
            }
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name); // 设置线程名称
            try {
                Parser parser = new Parser();
                uavVehicleDto.setVehicleId(Long.valueOf(name.split("#")[0]));
                // 截取IP地址和端口号
                String[] parts = name.split("#")[1].split(":");
                String ip = parts[0];
                int port = Integer.parseInt(parts[1]);
                Socket socket = new Socket(ip, port);
                InputStream stream = socket.getInputStream();
                int byteRead;
                while ((byteRead = stream.read()) != -1) {
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("任务 {} 被中断，正在退出", name);
                        // 清理资源（如关闭Socket等）
                        try {
                            if (socket != null && !socket.isClosed()) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            log.error("关闭Socket时发生异常: {}", e.getMessage(), e);
                        }
                        break;
                    }
                    MAVLinkPacket mavLinkPacket = parser.mavlink_parse_char(byteRead);
                    if (mavLinkPacket != null) {
                        System.out.println("mavLinkPacket:  " + mavLinkPacket.toString());
                        switch (mavLinkPacket.msgid) {
                            /** 检测信号消息显示系统或组件存在并正在响应。
                             type 和 autopilot 字段（以及消息组件 id）允许接收系统适当地处理来自该系统的进一步消息（例如，通过基于 autopilot 的用户界面布局）。
                             此微服务记录在 https://mavlink.io/en/services/heartbeat.html
                             * */
                            case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT: // 心跳包
                                msg_heartbeat heartbeat = (msg_heartbeat) mavLinkPacket.unpack();
                                System.out.println("msg_heartbeat: " + heartbeat.toString());
                                msg_command_long msg_command_long = new msg_command_long();
                                MAVLinkPacket pack = heartbeat.pack();
                                sysId = heartbeat.sysid;
                                System.out.println("uavVehicleDto: " + uavVehicleDto.toString());
                                uavVehicleDto.setCustomMode(heartbeat.custom_mode);
                                if (uavVehicleDto != null && uavVehicleDto.getVehicleSoc() > 0) {
                                    // 发消息
                                    msgService.handleMessage(SocketMethod.VEHICLEINFO.value() + "#" + JSONHelper.stringify(uavVehicleDto));
                                }
                                break;
                            /** 常规系统状态。如果系统遵循 MAVLink 标准，则系统状态主要由三种正交状态/模式定义：
                             * 系统模式，即 LOCKED（电机关闭和锁定）、MANUAL（RC 控制下的系统）、GUIDED（具有自主位置控制、位置设定点手动控制的系统）
                             * 或 AUTO（由路径/航路点规划器引导的系统）。NAV_MODE定义了当前的飞行状态：LIFTOFF（通常是开环机动）、着陆、WAYPOINTS 或 VECTOR。这表示内部导航状态机。
                             * 系统状态显示系统当前是否处于活动状态，以及是否发生了紧急情况。在 CRITICAL 和 EMERGENCY 状态下，
                             * MAV 仍被视为活动状态，但应自动启动紧急程序。发生故障后，它应首先从活动状态变为紧急状态，以允许人工干预，然后在一定超时后状态变为紧急状态。
                             **/
                            case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:
                                msg_sys_status sys_status = (msg_sys_status) mavLinkPacket.unpack();
                                //vehicleInfoDto.setVehicleSoc((double) sys_status.voltage_battery);
                                System.out.println("sys_status: " + sys_status.toString());
                                break;
                            /**
                             * 系统时间是主时钟的时间，通常是主机载计算机的计算机时钟。
                             * */
                            case msg_system_time.MAVLINK_MSG_ID_SYSTEM_TIME:
                                msg_system_time msg_system_time = (msg_system_time) mavLinkPacket.unpack();
                                System.out.println("msg_system_time: " + msg_system_time.toString());
                                break;
                            /**
                             * 全球定位系统 （GPS） 返回的全局位置,
                             * 这不是系统的全局位置估计值，而是 RAW 传感器值。有关全局位置估计，请参阅消息 GLOBAL_POSITION_INT。
                             * */
                            case msg_gps_raw_int.MAVLINK_MSG_ID_GPS_RAW_INT: // GPS
                                msg_gps_raw_int msg_gps_raw_int = (msg_gps_raw_int) mavLinkPacket.unpack();
                                System.out.println("msg_gps_raw_int: " + msg_gps_raw_int.toString());
                                break;

                            /**
                             * 9DOF 传感器的 RAW IMU 读数，由 id（默认 IMU1）标识。此消息应始终包含真实的原始值，不进行任何缩放，以允许数据捕获和系统调试。
                             **/
                            case msg_raw_imu.MAVLINK_MSG_ID_RAW_IMU:
                                msg_raw_imu msg_raw_imu = (msg_raw_imu) mavLinkPacket.unpack();
                                System.out.println("msg_raw_imu: " + msg_raw_imu.toString());
                                break;
                            /**
                             * 一个绝压和差压传感器的典型设置的压力读数。单位与每个字段中的指定相同。
                             **/
                            case msg_scaled_pressure.MAVLINK_MSG_ID_SCALED_PRESSURE: // 压力
                                msg_scaled_pressure msg_scaled_pressure = (msg_scaled_pressure) mavLinkPacket.unpack();
                                System.out.println("msg_scaled_pressure: " + msg_scaled_pressure.toString());
                                break;
                            /**
                             * 航空坐标系中的姿态（右手、Z 轴下、Y 轴右、X 轴前、ZYX、内在）。
                             * */
                            case msg_attitude.MAVLINK_MSG_ID_ATTITUDE: // 姿态
                                msg_attitude msg_attitude = (msg_attitude) mavLinkPacket.unpack();
                                System.out.println("msg_attitude: " + msg_attitude.toString());
                                break;
                            /**
                             *过滤后的全局位置（例如，融合的 GPS 和加速度计）。位置在 GPS 框架中（右手，Z 轴向上）。它
                             *被设计为缩放整数消息，因为 float 的分辨率不够。
                             * */
                            case msg_global_position_int.MAVLINK_MSG_ID_GLOBAL_POSITION_INT: // 位置信息
                                msg_global_position_int msg_global_position_int = (msg_global_position_int) mavLinkPacket.unpack();
                                if (msg_global_position_int.alt != 0) {
                                    uavVehicleDto.setVehicleAlt((double) msg_global_position_int.alt / 1000);
                                }
                                if (msg_global_position_int.lon > 0 && msg_global_position_int.lat > 0) {
                                    uavVehicleDto.setVehicleLong((double) (msg_global_position_int.lon / 10000000));
                                    uavVehicleDto.setVehicleLat((double) (msg_global_position_int.lat / 10000000));
                                }
                                System.out.println("msg_global_position_int: " + msg_global_position_int.toString());
                                break;
                            /**
                             * 被 ACTUATOR_OUTPUT_STATUS 取代。伺服输出的 RAW 值（对于来自遥控器的 RC 输入，请使用 RC_CHANNELS 消息）。
                             * 标准 PPM 调制如下：1000 微秒：0%，2000 微秒：100%。
                             * */
                            case msg_servo_output_raw.MAVLINK_MSG_ID_SERVO_OUTPUT_RAW:
                                msg_servo_output_raw msg_servo_output_raw = (msg_servo_output_raw) mavLinkPacket.unpack();
                                System.out.println("msg_servo_output_raw: " + msg_servo_output_raw.toString());
                                break;
                            /**
                             * 宣布当前目标任务项的序列号的消息（当任务运行时，系统将飞向/执行该序列号）。
                             * 此消息应始终流式传输 （名义上为 1Hz）。
                             * 此消息应在调用 MAV_CMD_DO_SET_MISSION_CURRENT 或 SET_MISSION_CURRENT 后发出。*/
                            case msg_mission_current.MAVLINK_MSG_ID_MISSION_CURRENT:
                                msg_mission_current msg_mission_current = (msg_mission_current) mavLinkPacket.unpack();
                                System.out.println("msg_mission_current: " + msg_mission_current.toString());
                                break;
                            /**
                             * 导航和位置控制器的状态。
                             * */
                            case msg_nav_controller_output.MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT:
                                msg_nav_controller_output msg_nav_controller_output = (msg_nav_controller_output) mavLinkPacket.unpack();
                                System.out.println("msg_nav_controller_output: " + msg_nav_controller_output.toString());
                                break;
                            /**
                             *接收的 RC 通道的 PPM 值。标准 PPM 调制如下：1000 微秒：0%，2000 微秒：100%。 值 UINT16_MAX 表示通道未使用。单个接收器/发射器可能违反此规范。
                             * */
                            case msg_rc_channels.MAVLINK_MSG_ID_RC_CHANNELS:
                                msg_rc_channels msg_rc_channels = (msg_rc_channels) mavLinkPacket.unpack();
                                System.out.println("msg_rc_channels: " + msg_rc_channels.toString());
                                break;
                            /**
                             * 固定翼飞机的 HUD 上通常显示的指标。
                             * */
                            case msg_vfr_hud.MAVLINK_MSG_ID_VFR_HUD:
                                msg_vfr_hud msg_vfr_hud = (msg_vfr_hud) mavLinkPacket.unpack();
                                uavVehicleDto.setVehicleSpeed((double) msg_vfr_hud.airspeed);
                                System.out.println("msg_vfr_hud: " + msg_vfr_hud.toString());
                                break;
                            /**
                             *
                             * */
                            case msg_timesync.MAVLINK_MSG_ID_TIMESYNC:
                                msg_timesync msg_timesync = (msg_timesync) mavLinkPacket.unpack();
                                System.out.println("msg_timesync: " + msg_timesync.toString());
                                break;
                            /**
                             * 辅助 9DOF 传感器设置的 RAW IMU 读数。此消息应包含按所述单位缩放的值
                             * */
                            case msg_scaled_imu2.MAVLINK_MSG_ID_SCALED_IMU2:
                                msg_scaled_imu2 msg_scaled_imu2 = (msg_scaled_imu2) mavLinkPacket.unpack();
                                System.out.println("msg_scaled_imu2: " + msg_scaled_imu2.toString());
                                break;
                            /**
                             * 电源状态
                             * */
                            case msg_power_status.MAVLINK_MSG_ID_POWER_STATUS: // 电池
                                msg_power_status msg_power_status = (msg_power_status) mavLinkPacket.unpack();
                                //vehicleInfoDto.setVehicleSoc((double) msg_power_status.Vservo);
                                System.out.println("msg_power_status: " + msg_power_status.toString());
                                break;
                            /**
                             * 从无人机流式传输以报告地形图下载的进度（由 TERRAIN_REQUEST 发起），
                             * 或作为对 TERRAIN_CHECK 请求的响应发送。请参阅 terrain 协议文档：https://mavlink.io/en/services/terrain.html
                             * */
                            case msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT:
                                msg_terrain_report msg_terrain_report = (msg_terrain_report) mavLinkPacket.unpack();
                                System.out.println("msg_terrain_report: " + msg_terrain_report.toString());
                                break;
                            /**
                             *
                             * 电池信息。更新 GCS 和飞行控制器电池状态。智能电池也会使用此消息，但可能会额外发送BATTERY_INFO。
                             * */
                            case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS: // 电池
                                msg_battery_status batteryStatus = (msg_battery_status) mavLinkPacket.unpack();
                                uavVehicleDto.setVehicleSoc((int) batteryStatus.battery_remaining);
                                System.out.println("msg_battery_status: " + batteryStatus.toString());
                                break;
                            /**
                             * 自动驾驶 RAM 的状态
                             * */
                            case msg_meminfo.MAVLINK_MSG_ID_MEMINFO:
                                msg_meminfo msg_meminfo = (msg_meminfo) mavLinkPacket.unpack();
                                System.out.println("msg_meminfo: " + msg_meminfo.toString());
                                break;
                            /**
                             * DCM 姿态估计器的状态
                             * */
                            case msg_ahrs.MAVLINK_MSG_ID_AHRS:
                                msg_ahrs msg_ahrs = (msg_ahrs) mavLinkPacket.unpack();
                                System.out.println("msg_ahrs: " + msg_ahrs.toString());
                                break;
                            /**
                             * EKF Status 消息，包括标志和变化。
                             * */
                            case msg_ekf_status_report.MAVLINK_MSG_ID_EKF_STATUS_REPORT:
                                msg_ekf_status_report msg_ekf_status_report = (msg_ekf_status_report) mavLinkPacket.unpack();
                                System.out.println("msg_ekf_status_report: " + msg_ekf_status_report.toString());
                                break;
                            /**
                             * RPM 传感器输出
                             * */
                            case msg_rpm.MAVLINK_MSG_ID_RPM:
                                msg_rpm msg_rpm = (msg_rpm) mavLinkPacket.unpack();
                                System.out.println("msg_rpm: " + msg_rpm.toString());
                                break;
                            /**
                             * 振动水平和加速度计削波 */
                            case msg_vibration.MAVLINK_MSG_ID_VIBRATION:
                                msg_vibration msg_vibration = (msg_vibration) mavLinkPacket.unpack();
                                System.out.println("msg_vibration: " + msg_vibration.toString());
                                break;
                            /**
                             *状态文本消息。这些消息在 QGroundControl 的 COMM 控制台中以黄色打印。
                             *警告： 它们会消耗相当多的带宽，因此仅用于重要的状态和错误消息。如果实施得当，这些消息将缓冲在 MCU 上，
                             *并且仅以有限的速率（例如 10 Hz）发送。
                             * */
                            case msg_statustext.MAVLINK_MSG_ID_STATUSTEXT:
                                msg_statustext msg_statustext = (msg_statustext) mavLinkPacket.unpack();
                                System.out.println("msg_statustext: " + msg_statustext.toString());
                                break;
                            /**
                             * 报告命令的状态。包括命令是否已执行的反馈。命令 microservice
                             * 记录在 https://mavlink.io/en/services/command.html
                             */
                            case msg_command_ack.MAVLINK_MSG_ID_COMMAND_ACK:
                                msg_command_ack msg_command_ack = (msg_command_ack) mavLinkPacket.unpack();
                                System.out.println("msg_command_ack: " + msg_command_ack.toString());
                                break;
                            default:
                                System.out.println("defaultTest: " + mavLinkPacket.unpack().toString());
                                break;
                        }

                        if (lockFlag != 0) {
                            lockOrUnLock(socket, arm);
                        }
                        if (modeFlag != 0) {
                            setMode(socket, uavMode);
                        }
                        if (safeTakeOffFlag != 0) {
                            takeOff(socket, takeOffAltitude);
                        }
                        if (landFlag != 0) {
                            onLand(socket);
                        }
                        if (cmdFlag != 0) {
                            if (cmdCallback != null) {
                                cmdCallback.command(socket, sysId);
                            }
                        }
                        if (launchFlag != 0) {
                            onLaunch(socket);
                        }
                        if (addMissionFlag != 0) {
                            addMissions(socket);
                        }
                        if (startMissionFlag != 0) {
                            startMission(socket);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("任务 {} 被中断，正在清理资源", name);
                }
            }
        }
    }

    public static void cancelTaskByName(Map<String, Future<?>> taskMap, String name) {
        Future<?> future = taskMap.get(name);
        if (future != null) {
            future.cancel(true); // 取消任务并中断线程
            System.out.println("已取消任务: " + name);
        } else {
            System.out.println("未找到任务: " + name);
        }
    }
}
