SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `blog`
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('1', 'author1', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('2', 'author2', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('3', 'author3', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('4', 'author4', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('5', 'author5', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('6', 'author6', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('7', 'author7', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('8', 'author8', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('9', 'author9', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('10', 'author10', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('11', 'author11', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('12', 'author12', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('13', 'author13', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('14', 'author14', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('15', 'author15', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('16', 'author16', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('17', 'author17', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('18', 'author18', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('19', 'author19', '2014-11-10 16:29:02');
INSERT INTO `blog` VALUES ('20', 'author20', '2014-11-10 16:29:02');
