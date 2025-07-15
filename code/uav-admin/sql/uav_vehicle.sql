/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : czuft_uav

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 02/09/2024 13:47:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for uav_vehicle
-- ----------------------------
DROP TABLE IF EXISTS `uav_vehicle`;
CREATE TABLE `uav_vehicle`  (
  `vehicle_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '无人机设备ID',
  `vehicle_mac` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '无人机mac地址',
  `vehicle_speed` decimal(32, 0) NOT NULL COMMENT '无人机速度 km/h',
  `vehicle_soc` decimal(32, 0) NOT NULL COMMENT '无人机电量',
  `vehicle_long` decimal(32, 0) NOT NULL COMMENT '无人机经度',
  `vehicleLat` decimal(32, 0) NOT NULL COMMENT '无人机纬度',
  `vehicleAlt` decimal(32, 0) NOT NULL COMMENT '无人机海拔高度',
  `faultInfo` int(11) NOT NULL COMMENT '错误信息',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`vehicle_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '无人机实时信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of uav_vehicle
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
