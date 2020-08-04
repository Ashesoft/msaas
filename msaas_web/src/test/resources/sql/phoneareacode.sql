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

 Date: 14/07/2020 14:24:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for phoneareacode
-- ----------------------------
DROP TABLE IF EXISTS `phoneareacode`;
CREATE TABLE `phoneareacode`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `createtime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `areaname` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域名称',
  `areacode` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 192 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phoneareacode
-- ----------------------------
INSERT INTO `phoneareacode` VALUES (1, '2020-06-02 11:21:11', '中国', '+86');
INSERT INTO `phoneareacode` VALUES (2, '2020-06-02 11:21:11', '中国香港', '+852');
INSERT INTO `phoneareacode` VALUES (3, '2020-06-02 11:21:11', '中国澳门', '+853');
INSERT INTO `phoneareacode` VALUES (4, '2020-06-02 11:21:11', '中国台湾', '+886');
INSERT INTO `phoneareacode` VALUES (5, '2020-06-02 11:21:11', '日本', '+81');
INSERT INTO `phoneareacode` VALUES (6, '2020-06-02 11:21:11', '韩国', '+82');
INSERT INTO `phoneareacode` VALUES (7, '2020-06-02 11:21:11', '英国', '+44');
INSERT INTO `phoneareacode` VALUES (8, '2020-06-02 11:21:11', '美国', '+1');
INSERT INTO `phoneareacode` VALUES (9, '2020-06-02 11:21:11', '加拿大', '+1');
INSERT INTO `phoneareacode` VALUES (10, '2020-06-02 11:21:11', '法国', '+33');
INSERT INTO `phoneareacode` VALUES (11, '2020-06-02 11:21:11', '芬兰', '+358');
INSERT INTO `phoneareacode` VALUES (12, '2020-06-02 11:21:11', '德国', '+49');
INSERT INTO `phoneareacode` VALUES (13, '2020-06-02 11:21:11', '荷兰', '+31');
INSERT INTO `phoneareacode` VALUES (14, '2020-06-02 11:21:11', '意大利', '+39');
INSERT INTO `phoneareacode` VALUES (15, '2020-06-02 11:21:11', '印度', '+91');
INSERT INTO `phoneareacode` VALUES (16, '2020-06-02 11:21:11', '泰国', '+66');
INSERT INTO `phoneareacode` VALUES (17, '2020-06-02 11:21:11', '新西兰', '+64');
INSERT INTO `phoneareacode` VALUES (18, '2020-06-02 11:21:11', '葡萄牙', '+351');
INSERT INTO `phoneareacode` VALUES (19, '2020-06-02 11:21:11', '俄罗斯', '+7');
INSERT INTO `phoneareacode` VALUES (20, '2020-06-02 11:21:11', '新加坡', '+65');
INSERT INTO `phoneareacode` VALUES (21, '2020-06-02 11:21:11', '马来西亚', '+60');
INSERT INTO `phoneareacode` VALUES (22, '2020-06-02 11:21:11', '西班牙', '+34');
INSERT INTO `phoneareacode` VALUES (23, '2020-06-02 11:21:11', '南非', '+27');
INSERT INTO `phoneareacode` VALUES (24, '2020-06-02 11:21:11', '瑞典', '+46');
INSERT INTO `phoneareacode` VALUES (25, '2020-06-02 11:21:11', '安哥拉', '+244');
INSERT INTO `phoneareacode` VALUES (26, '2020-06-02 11:21:11', '阿富汗', '+93');
INSERT INTO `phoneareacode` VALUES (27, '2020-06-02 11:21:11', '阿尔巴尼亚', '+355');
INSERT INTO `phoneareacode` VALUES (28, '2020-06-02 11:21:11', '阿尔及利亚', '+213');
INSERT INTO `phoneareacode` VALUES (29, '2020-06-02 11:21:11', '安道尔共和国', '+376');
INSERT INTO `phoneareacode` VALUES (30, '2020-06-02 11:21:11', '安圭拉岛', '+1264');
INSERT INTO `phoneareacode` VALUES (31, '2020-06-02 11:21:11', '安提瓜和巴布达', '+1268');
INSERT INTO `phoneareacode` VALUES (32, '2020-06-02 11:21:11', '阿根廷', '+54');
INSERT INTO `phoneareacode` VALUES (33, '2020-06-02 11:21:11', '亚美尼亚', '+374');
INSERT INTO `phoneareacode` VALUES (34, '2020-06-02 11:21:11', '阿森松', '+247');
INSERT INTO `phoneareacode` VALUES (35, '2020-06-02 11:21:11', '澳大利亚', '+61');
INSERT INTO `phoneareacode` VALUES (36, '2020-06-02 11:21:11', '奥地利', '+43');
INSERT INTO `phoneareacode` VALUES (37, '2020-06-02 11:21:11', '阿塞拜疆', '+994');
INSERT INTO `phoneareacode` VALUES (38, '2020-06-02 11:21:11', '巴哈马', '+1242');
INSERT INTO `phoneareacode` VALUES (39, '2020-06-02 11:21:11', '巴林', '+973');
INSERT INTO `phoneareacode` VALUES (40, '2020-06-02 11:21:11', '孟加拉国', '+880');
INSERT INTO `phoneareacode` VALUES (41, '2020-06-02 11:21:11', '巴巴多斯', '+1246');
INSERT INTO `phoneareacode` VALUES (42, '2020-06-02 11:21:11', '白俄罗斯', '+375');
INSERT INTO `phoneareacode` VALUES (43, '2020-06-02 11:21:11', '比利时', '+32');
INSERT INTO `phoneareacode` VALUES (44, '2020-06-02 11:21:11', '伯利兹', '+501');
INSERT INTO `phoneareacode` VALUES (45, '2020-06-02 11:21:11', '贝宁', '+229');
INSERT INTO `phoneareacode` VALUES (46, '2020-06-02 11:21:11', '百慕大群岛', '+1441');
INSERT INTO `phoneareacode` VALUES (47, '2020-06-02 11:21:11', '玻利维亚', '+591');
INSERT INTO `phoneareacode` VALUES (48, '2020-06-02 11:21:11', '博茨瓦纳', '+267');
INSERT INTO `phoneareacode` VALUES (49, '2020-06-02 11:21:11', '巴西', '+55');
INSERT INTO `phoneareacode` VALUES (50, '2020-06-02 11:21:11', '文莱', '+673');
INSERT INTO `phoneareacode` VALUES (51, '2020-06-02 11:21:11', '保加利亚', '+359');
INSERT INTO `phoneareacode` VALUES (52, '2020-06-02 11:21:11', '布基纳法索', '+226');
INSERT INTO `phoneareacode` VALUES (53, '2020-06-02 11:21:11', '缅甸', '+95');
INSERT INTO `phoneareacode` VALUES (54, '2020-06-02 11:21:11', '布隆迪', '+257');
INSERT INTO `phoneareacode` VALUES (55, '2020-06-02 11:21:11', '喀麦隆', '+237');
INSERT INTO `phoneareacode` VALUES (56, '2020-06-02 11:21:11', '开曼群岛', '+1345');
INSERT INTO `phoneareacode` VALUES (57, '2020-06-02 11:21:11', '中非共和国', '+236');
INSERT INTO `phoneareacode` VALUES (58, '2020-06-02 11:21:11', '乍得', '+235');
INSERT INTO `phoneareacode` VALUES (59, '2020-06-02 11:21:11', '智利', '+56');
INSERT INTO `phoneareacode` VALUES (60, '2020-06-02 11:21:11', '哥伦比亚', '+57');
INSERT INTO `phoneareacode` VALUES (61, '2020-06-02 11:21:11', '刚果', '+242');
INSERT INTO `phoneareacode` VALUES (62, '2020-06-02 11:21:11', '库克群岛', '+682');
INSERT INTO `phoneareacode` VALUES (63, '2020-06-02 11:21:11', '哥斯达黎加', '+506');
INSERT INTO `phoneareacode` VALUES (64, '2020-06-02 11:21:11', '古巴', '+53');
INSERT INTO `phoneareacode` VALUES (65, '2020-06-02 11:21:11', '塞浦路斯', '+357');
INSERT INTO `phoneareacode` VALUES (66, '2020-06-02 11:21:11', '捷克', '+420');
INSERT INTO `phoneareacode` VALUES (67, '2020-06-02 11:21:11', '丹麦', '+45');
INSERT INTO `phoneareacode` VALUES (68, '2020-06-02 11:21:11', '吉布提', '+253');
INSERT INTO `phoneareacode` VALUES (69, '2020-06-02 11:21:11', '多米尼加共和国', '+1890');
INSERT INTO `phoneareacode` VALUES (70, '2020-06-02 11:21:11', '厄瓜多尔', '+593');
INSERT INTO `phoneareacode` VALUES (71, '2020-06-02 11:21:11', '埃及', '+20');
INSERT INTO `phoneareacode` VALUES (72, '2020-06-02 11:21:11', '萨尔瓦多', '+503');
INSERT INTO `phoneareacode` VALUES (73, '2020-06-02 11:21:11', '爱沙尼亚', '+372');
INSERT INTO `phoneareacode` VALUES (74, '2020-06-02 11:21:11', '埃塞俄比亚', '+251');
INSERT INTO `phoneareacode` VALUES (75, '2020-06-02 11:21:11', '斐济', '+679');
INSERT INTO `phoneareacode` VALUES (76, '2020-06-02 11:21:11', '法属圭亚那', '+594');
INSERT INTO `phoneareacode` VALUES (77, '2020-06-02 11:21:11', '加蓬', '+241');
INSERT INTO `phoneareacode` VALUES (78, '2020-06-02 11:21:11', '冈比亚', '+220');
INSERT INTO `phoneareacode` VALUES (79, '2020-06-02 11:21:11', '格鲁吉亚', '+995');
INSERT INTO `phoneareacode` VALUES (80, '2020-06-02 11:21:11', '加纳', '+233');
INSERT INTO `phoneareacode` VALUES (81, '2020-06-02 11:21:11', '直布罗陀', '+350');
INSERT INTO `phoneareacode` VALUES (82, '2020-06-02 11:21:11', '希腊', '+30');
INSERT INTO `phoneareacode` VALUES (83, '2020-06-02 11:21:11', '格林纳达', '+1473');
INSERT INTO `phoneareacode` VALUES (84, '2020-06-02 11:21:11', '关岛', '+1671');
INSERT INTO `phoneareacode` VALUES (85, '2020-06-02 11:21:11', '危地马拉', '+502');
INSERT INTO `phoneareacode` VALUES (86, '2020-06-02 11:21:11', '几内亚', '+224');
INSERT INTO `phoneareacode` VALUES (87, '2020-06-02 11:21:11', '圭亚那', '+592');
INSERT INTO `phoneareacode` VALUES (88, '2020-06-02 11:21:11', '海地', '+509');
INSERT INTO `phoneareacode` VALUES (89, '2020-06-02 11:21:11', '洪都拉斯', '+504');
INSERT INTO `phoneareacode` VALUES (90, '2020-06-02 11:21:11', '匈牙利', '+36');
INSERT INTO `phoneareacode` VALUES (91, '2020-06-02 11:21:11', '冰岛', '+354');
INSERT INTO `phoneareacode` VALUES (92, '2020-06-02 11:21:11', '印度尼西亚', '+62');
INSERT INTO `phoneareacode` VALUES (93, '2020-06-02 11:21:11', '伊朗', '+98');
INSERT INTO `phoneareacode` VALUES (94, '2020-06-02 11:21:11', '伊拉克', '+964');
INSERT INTO `phoneareacode` VALUES (95, '2020-06-02 11:21:11', '爱尔兰', '+353');
INSERT INTO `phoneareacode` VALUES (96, '2020-06-02 11:21:11', '以色列', '+972');
INSERT INTO `phoneareacode` VALUES (97, '2020-06-02 11:21:11', '科特迪瓦', '+225');
INSERT INTO `phoneareacode` VALUES (98, '2020-06-02 11:21:11', '牙买加', '+1876');
INSERT INTO `phoneareacode` VALUES (99, '2020-06-02 11:21:11', '约旦', '+962');
INSERT INTO `phoneareacode` VALUES (100, '2020-06-02 11:21:11', '柬埔寨', '+855');
INSERT INTO `phoneareacode` VALUES (101, '2020-06-02 11:21:11', '哈萨克斯坦', '+7');
INSERT INTO `phoneareacode` VALUES (102, '2020-06-02 11:21:11', '肯尼亚', '+254');
INSERT INTO `phoneareacode` VALUES (103, '2020-06-02 11:21:11', '科威特', '+965');
INSERT INTO `phoneareacode` VALUES (104, '2020-06-02 11:21:11', '吉尔吉斯坦', '+996');
INSERT INTO `phoneareacode` VALUES (105, '2020-06-02 11:21:11', '老挝', '+856');
INSERT INTO `phoneareacode` VALUES (106, '2020-06-02 11:21:11', '拉脱维亚', '+371');
INSERT INTO `phoneareacode` VALUES (107, '2020-06-02 11:21:11', '黎巴嫩', '+961');
INSERT INTO `phoneareacode` VALUES (108, '2020-06-02 11:21:11', '莱索托', '+266');
INSERT INTO `phoneareacode` VALUES (109, '2020-06-02 11:21:11', '利比里亚', '+231');
INSERT INTO `phoneareacode` VALUES (110, '2020-06-02 11:21:11', '利比亚', '+218');
INSERT INTO `phoneareacode` VALUES (111, '2020-06-02 11:21:11', '列支敦士登', '+423');
INSERT INTO `phoneareacode` VALUES (112, '2020-06-02 11:21:11', '立陶宛', '+370');
INSERT INTO `phoneareacode` VALUES (113, '2020-06-02 11:21:11', '卢森堡', '+352');
INSERT INTO `phoneareacode` VALUES (114, '2020-06-02 11:21:11', '马达加斯加', '+261');
INSERT INTO `phoneareacode` VALUES (115, '2020-06-02 11:21:11', '马拉维', '+265');
INSERT INTO `phoneareacode` VALUES (116, '2020-06-02 11:21:11', '马尔代夫', '+960');
INSERT INTO `phoneareacode` VALUES (117, '2020-06-02 11:21:11', '马里', '+223');
INSERT INTO `phoneareacode` VALUES (118, '2020-06-02 11:21:11', '马耳他', '+356');
INSERT INTO `phoneareacode` VALUES (119, '2020-06-02 11:21:11', '马里亚那群岛', '+1670');
INSERT INTO `phoneareacode` VALUES (120, '2020-06-02 11:21:11', '马提尼克', '+596');
INSERT INTO `phoneareacode` VALUES (121, '2020-06-02 11:21:11', '毛里求斯', '+230');
INSERT INTO `phoneareacode` VALUES (122, '2020-06-02 11:21:11', '墨西哥', '+52');
INSERT INTO `phoneareacode` VALUES (123, '2020-06-02 11:21:11', '摩尔多瓦', '+373');
INSERT INTO `phoneareacode` VALUES (124, '2020-06-02 11:21:11', '摩纳哥', '+377');
INSERT INTO `phoneareacode` VALUES (125, '2020-06-02 11:21:11', '蒙古', '+976');
INSERT INTO `phoneareacode` VALUES (126, '2020-06-02 11:21:11', '蒙特塞拉特岛', '+1664');
INSERT INTO `phoneareacode` VALUES (127, '2020-06-02 11:21:11', '摩洛哥', '+212');
INSERT INTO `phoneareacode` VALUES (128, '2020-06-02 11:21:11', '莫桑比克', '+258');
INSERT INTO `phoneareacode` VALUES (129, '2020-06-02 11:21:11', '纳米比亚', '+264');
INSERT INTO `phoneareacode` VALUES (130, '2020-06-02 11:21:11', '瑙鲁', '+674');
INSERT INTO `phoneareacode` VALUES (131, '2020-06-02 11:21:11', '尼泊尔', '+977');
INSERT INTO `phoneareacode` VALUES (132, '2020-06-02 11:21:11', '荷属安的列斯', '+599');
INSERT INTO `phoneareacode` VALUES (133, '2020-06-02 11:21:11', '尼加拉瓜', '+505');
INSERT INTO `phoneareacode` VALUES (134, '2020-06-02 11:21:11', '尼日尔', '+227');
INSERT INTO `phoneareacode` VALUES (135, '2020-06-02 11:21:11', '尼日利亚', '+234');
INSERT INTO `phoneareacode` VALUES (136, '2020-06-02 11:21:11', '朝鲜', '+850');
INSERT INTO `phoneareacode` VALUES (137, '2020-06-02 11:21:11', '挪威', '+47');
INSERT INTO `phoneareacode` VALUES (138, '2020-06-02 11:21:11', '阿曼', '+968');
INSERT INTO `phoneareacode` VALUES (139, '2020-06-02 11:21:11', '巴基斯坦', '+92');
INSERT INTO `phoneareacode` VALUES (140, '2020-06-02 11:21:11', '巴拿马', '+507');
INSERT INTO `phoneareacode` VALUES (141, '2020-06-02 11:21:11', '巴布亚新几内亚', '+675');
INSERT INTO `phoneareacode` VALUES (142, '2020-06-02 11:21:11', '巴拉圭', '+595');
INSERT INTO `phoneareacode` VALUES (143, '2020-06-02 11:21:11', '秘鲁', '+51');
INSERT INTO `phoneareacode` VALUES (144, '2020-06-02 11:21:11', '菲律宾', '+63');
INSERT INTO `phoneareacode` VALUES (145, '2020-06-02 11:21:11', '波兰', '+48');
INSERT INTO `phoneareacode` VALUES (146, '2020-06-02 11:21:11', '法属玻利尼西亚', '+689');
INSERT INTO `phoneareacode` VALUES (147, '2020-06-02 11:21:11', '波多黎各', '+1787');
INSERT INTO `phoneareacode` VALUES (148, '2020-06-02 11:21:11', '卡塔尔', '+974');
INSERT INTO `phoneareacode` VALUES (149, '2020-06-02 11:21:11', '留尼旺', '+262');
INSERT INTO `phoneareacode` VALUES (150, '2020-06-02 11:21:11', '罗马尼亚', '+40');
INSERT INTO `phoneareacode` VALUES (151, '2020-06-02 11:21:11', '圣卢西亚', '+1758');
INSERT INTO `phoneareacode` VALUES (152, '2020-06-02 11:21:11', '圣文森特岛', '+1784');
INSERT INTO `phoneareacode` VALUES (153, '2020-06-02 11:21:11', '东萨摩亚(美)', '+684');
INSERT INTO `phoneareacode` VALUES (154, '2020-06-02 11:21:11', '西萨摩亚', '+685');
INSERT INTO `phoneareacode` VALUES (155, '2020-06-02 11:21:11', '圣马力诺', '+378');
INSERT INTO `phoneareacode` VALUES (156, '2020-06-02 11:21:11', '圣多美和普林西比', '+239');
INSERT INTO `phoneareacode` VALUES (157, '2020-06-02 11:21:11', '沙特阿拉伯', '+966');
INSERT INTO `phoneareacode` VALUES (158, '2020-06-02 11:21:11', '塞内加尔', '+221');
INSERT INTO `phoneareacode` VALUES (159, '2020-06-02 11:21:11', '塞舌尔', '+248');
INSERT INTO `phoneareacode` VALUES (160, '2020-06-02 11:21:11', '塞拉利昂', '+232');
INSERT INTO `phoneareacode` VALUES (161, '2020-06-02 11:21:11', '斯洛伐克', '+421');
INSERT INTO `phoneareacode` VALUES (162, '2020-06-02 11:21:11', '斯洛文尼亚', '+386');
INSERT INTO `phoneareacode` VALUES (163, '2020-06-02 11:21:11', '所罗门群岛', '+677');
INSERT INTO `phoneareacode` VALUES (164, '2020-06-02 11:21:11', '索马里', '+252');
INSERT INTO `phoneareacode` VALUES (165, '2020-06-02 11:21:11', '斯里兰卡', '+94');
INSERT INTO `phoneareacode` VALUES (166, '2020-06-02 11:21:11', '圣卢西亚', '+1758');
INSERT INTO `phoneareacode` VALUES (167, '2020-06-02 11:21:11', '圣文森特', '+1784');
INSERT INTO `phoneareacode` VALUES (168, '2020-06-02 11:21:11', '苏丹', '+249');
INSERT INTO `phoneareacode` VALUES (169, '2020-06-02 11:21:11', '苏里南', '+597');
INSERT INTO `phoneareacode` VALUES (170, '2020-06-02 11:21:11', '斯威士兰', '+268');
INSERT INTO `phoneareacode` VALUES (171, '2020-06-02 11:21:11', '瑞士', '+41');
INSERT INTO `phoneareacode` VALUES (172, '2020-06-02 11:21:11', '叙利亚', '+963');
INSERT INTO `phoneareacode` VALUES (173, '2020-06-02 11:21:11', '塔吉克斯坦', '+992');
INSERT INTO `phoneareacode` VALUES (174, '2020-06-02 11:21:11', '坦桑尼亚', '+255');
INSERT INTO `phoneareacode` VALUES (175, '2020-06-02 11:21:11', '多哥', '+228');
INSERT INTO `phoneareacode` VALUES (176, '2020-06-02 11:21:11', '汤加', '+676');
INSERT INTO `phoneareacode` VALUES (177, '2020-06-02 11:21:11', '特立尼达和多巴哥', '+1868');
INSERT INTO `phoneareacode` VALUES (178, '2020-06-02 11:21:11', '突尼斯', '+216');
INSERT INTO `phoneareacode` VALUES (179, '2020-06-02 11:21:11', '土耳其', '+90');
INSERT INTO `phoneareacode` VALUES (180, '2020-06-02 11:21:11', '土库曼斯坦', '+993');
INSERT INTO `phoneareacode` VALUES (181, '2020-06-02 11:21:11', '乌干达', '+256');
INSERT INTO `phoneareacode` VALUES (182, '2020-06-02 11:21:11', '乌克兰', '+380');
INSERT INTO `phoneareacode` VALUES (183, '2020-06-02 11:21:11', '阿拉伯联合酋长国', '+971');
INSERT INTO `phoneareacode` VALUES (184, '2020-06-02 11:21:11', '乌拉圭', '+598');
INSERT INTO `phoneareacode` VALUES (185, '2020-06-02 11:21:11', '乌兹别克斯坦', '+998');
INSERT INTO `phoneareacode` VALUES (186, '2020-06-02 11:21:11', '委内瑞拉', '+58');
INSERT INTO `phoneareacode` VALUES (187, '2020-06-02 11:21:11', '越南', '+84');
INSERT INTO `phoneareacode` VALUES (188, '2020-06-02 11:21:11', '也门', '+967');
INSERT INTO `phoneareacode` VALUES (189, '2020-06-02 11:21:11', '南斯拉夫', '+381');
INSERT INTO `phoneareacode` VALUES (190, '2020-06-02 11:21:11', '津巴布韦', '+263');
INSERT INTO `phoneareacode` VALUES (191, '2020-06-02 11:21:11', '扎伊尔', '+243');
INSERT INTO `phoneareacode` VALUES (192, '2020-06-02 11:21:11', '赞比亚', '+260');

SET FOREIGN_KEY_CHECKS = 1;
