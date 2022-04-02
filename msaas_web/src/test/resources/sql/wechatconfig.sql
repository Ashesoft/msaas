/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : springboot

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 02/04/2022 10:17:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wechatconfig
-- ----------------------------
DROP TABLE IF EXISTS `wechatconfig`;
CREATE TABLE `wechatconfig`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `createtime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updatetime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `appid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用id',
  `appsecret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用密钥',
  `apptype` int(0) NULL DEFAULT 1 COMMENT '应用类型:\r\n1. 微信公众号\r\n2. 微信小程序\r\n3. 其他',
  `appstatus` int(0) NULL DEFAULT 1 COMMENT '应用状态: \r\n0. 禁用\r\n1. 启用\r\n2. 异常\r\n3. 注销\r\n',
  PRIMARY KEY (`id`, `appid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
