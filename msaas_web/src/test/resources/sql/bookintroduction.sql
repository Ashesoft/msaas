/*
 Navicat Premium Data Transfer

 Source Server         : mylocalsql
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : springboot

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 14/07/2020 14:24:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bookintroduction
-- ----------------------------
DROP TABLE IF EXISTS `bookintroduction`;
CREATE TABLE `bookintroduction`  (
  `bid` bigint(24) NOT NULL COMMENT '书籍编号',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `bname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍名称',
  `btitle` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍副名',
  `bauthor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍作者',
  `btime` datetime(0) NULL DEFAULT NULL COMMENT '发行时间',
  `bdescribe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍简介',
  `bimg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '书籍封面',
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bookintroduction
-- ----------------------------
INSERT INTO `bookintroduction` VALUES (1277884058818449407, '2020-06-01 11:26:46', '红楼梦', '脂砚斋重评石头记甲戌本', '曹雪芹', '1987-07-07 11:27:13', '本书与通行印本最大的不同之处在于：可以从中窥见曹雪芹生前创作这部小说的早期原貌，并可直接品味到作者的“红颜知已”脂砚斋在甲戌原稿本上留下的1600余条珍贵批语。这是打开《红楼梦》迷宫的一把钥匙！', '/img/story/hlm/1_140820121217_1.jpg');

SET FOREIGN_KEY_CHECKS = 1;
