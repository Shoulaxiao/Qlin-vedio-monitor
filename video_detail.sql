/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : videoDB

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 30/04/2023 15:52:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for video_detail
-- ----------------------------
DROP TABLE IF EXISTS `video_detail`;
CREATE TABLE `video_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `type` int NOT NULL DEFAULT '0' COMMENT '0-画面变动；1-有人移动',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '视频标题',
  `desciption` varchar(255) NOT NULL DEFAULT '' COMMENT '描述信息',
  `pre_view_img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '预览信息图',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '播放地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
