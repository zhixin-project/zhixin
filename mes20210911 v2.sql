/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : mes

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2021-09-11 19:22:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for manage
-- ----------------------------
DROP TABLE IF EXISTS `manage`;
CREATE TABLE `manage` (
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manage
-- ----------------------------
INSERT INTO `manage` VALUES ('jxzx', 'jxzx123456');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` char(16) NOT NULL,
  `custom_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `required_time` date NOT NULL,
  `customer` varchar(255) NOT NULL,
  `product_id` int NOT NULL,
  `amount` int NOT NULL,
  `started_production` tinyint NOT NULL DEFAULT '0',
  `curr_step` int NOT NULL DEFAULT '0',
  `total_step` int NOT NULL DEFAULT '0',
  `beizhu` varchar(255) DEFAULT NULL,
  `buxiadan` tinyint NOT NULL DEFAULT '0',
  `bxdly` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `tqck` int NOT NULL DEFAULT '0',
  `finished` tinyint NOT NULL DEFAULT '0',
  `actual_ship` int NOT NULL DEFAULT '0',
  `using_stock` int NOT NULL DEFAULT '0',
  `price` double(10,2) DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  `mbsl` int DEFAULT NULL,
  `touliao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for processes
-- ----------------------------
DROP TABLE IF EXISTS `processes`;
CREATE TABLE `processes` (
  `id` char(16) NOT NULL,
  `tag_id` int NOT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `operator` int DEFAULT NULL,
  `finished_time` datetime DEFAULT NULL,
  `sequence` int NOT NULL,
  PRIMARY KEY (`id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of processes
-- ----------------------------

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `size` varchar(255) DEFAULT NULL,
  `standard` varchar(255) DEFAULT NULL,
  `jianban` varchar(255) DEFAULT NULL,
  `luoliao` varchar(255) DEFAULT NULL,
  `chongyoucao` varchar(255) DEFAULT NULL,
  `chongkong` varchar(255) DEFAULT NULL,
  `dazi` varchar(255) DEFAULT NULL,
  `zx_hg` varchar(255) DEFAULT NULL,
  `zx_hg_t` varchar(255) DEFAULT NULL,
  `zx_hg_z` varchar(255) DEFAULT NULL,
  `zx_zk` varchar(255) DEFAULT NULL,
  `zx_sg` varchar(255) DEFAULT NULL,
  `zx_sg_t` varchar(255) DEFAULT NULL,
  `zx_sg_z` varchar(255) DEFAULT NULL,
  `zx_bh` varchar(255) DEFAULT NULL,
  `dj_wj` varchar(255) DEFAULT NULL,
  `dj_nj` varchar(255) DEFAULT NULL,
  `dj_gd` varchar(255) DEFAULT NULL,
  `dj_gd_t` varchar(255) DEFAULT NULL,
  `dj_gd_z` varchar(255) DEFAULT NULL,
  `kkf` varchar(255) DEFAULT NULL,
  `fanbian` varchar(255) DEFAULT NULL,
  `qlb` varchar(255) DEFAULT NULL,
  `yanmo` varchar(255) DEFAULT NULL,
  `paoguang` varchar(255) DEFAULT NULL,
  `shangyou` varchar(255) DEFAULT NULL,
  `used` tinyint NOT NULL DEFAULT '0',
  `houchuli` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `caizhi` varchar(255) DEFAULT NULL,
  `kucun` int NOT NULL DEFAULT '0',
  `maoban` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of products
-- ----------------------------

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tags
-- ----------------------------
INSERT INTO `tags` VALUES ('1', '剪板/落料');
INSERT INTO `tags` VALUES ('2', '冲(通)孔');
INSERT INTO `tags` VALUES ('3', '打字');
INSERT INTO `tags` VALUES ('4', '卷圆');
INSERT INTO `tags` VALUES ('5', '整形');
INSERT INTO `tags` VALUES ('6', '倒角');
INSERT INTO `tags` VALUES ('7', '抛光');
INSERT INTO `tags` VALUES ('8', '翻边');
INSERT INTO `tags` VALUES ('9', '包装');

-- ----------------------------
-- Table structure for tag_relates
-- ----------------------------
DROP TABLE IF EXISTS `tag_relates`;
CREATE TABLE `tag_relates` (
  `product_id` int NOT NULL,
  `tag_id` int NOT NULL,
  `sequence` int DEFAULT NULL,
  PRIMARY KEY (`product_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag_relates
-- ----------------------------

-- ----------------------------
-- Table structure for workers
-- ----------------------------
DROP TABLE IF EXISTS `workers`;
CREATE TABLE `workers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `worker_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of workers
-- ----------------------------
INSERT INTO `workers` VALUES ('0', '通用账户', '123456');
