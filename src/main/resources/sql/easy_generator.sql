/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 80021
Source Host           : 127.0.0.1:3306
Source Database       : easy_generator

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2021-04-08 17:45:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` bigint NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员表';

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES ('1', 'admin', 'admin', '2021-04-08 17:34:22', '2021-04-08 12:12:08');

-- ----------------------------
-- Table structure for t_database
-- ----------------------------
DROP TABLE IF EXISTS `t_database`;
CREATE TABLE `t_database` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `db_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名称',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'host',
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '端口',
  `drive` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '驱动',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `status` int DEFAULT NULL COMMENT '连接状态 0失败  1成功',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据库配置表';

-- ----------------------------
-- Records of t_database
-- ----------------------------
INSERT INTO `t_database` VALUES ('1', 'MYSQL', 'easy_generator', '127.0.0.1', '3306', 'com.mysql.cj.jdbc.Driver', 'root', '123456', '0', '2021-03-26 12:16:24', '2021-04-08 17:43:44');

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目名称',
  `root_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目根路径',
  `project_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目类型',
  `use_lombok` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '使用Lombok 0否 1是',
  `database_id` bigint DEFAULT NULL COMMENT '数据库连接ID',
  `table_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表前缀',
  `column_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段前缀',
  `table_name_style` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名映射策略',
  `column_name_style` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段名映射策略',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目模板配置表';

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('1', 'easy-generator项目', 'D:\\my-code\\easy-generator', 'SPRING_BOOT', '1', '1', 't_', '', 'underline_to_camel', 'underline_to_camel', '代码生成器EASY-GENERATOR', 'dwj', '2021-03-25 12:54:50', '2021-04-08 15:48:46');

-- ----------------------------
-- Table structure for t_project_template
-- ----------------------------
DROP TABLE IF EXISTS `t_project_template`;
CREATE TABLE `t_project_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint DEFAULT NULL COMMENT '项目ID',
  `template_id` bigint DEFAULT NULL COMMENT '代码模板',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '包名',
  `output_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '输出路径',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目代码模板配置中间表';

-- ----------------------------
-- Records of t_project_template
-- ----------------------------
INSERT INTO `t_project_template` VALUES ('1', '1', '1', 'com.dwj.generator.controller', 'D:\\my-code\\easy-generator\\src\\main\\java\\com\\dwj\\generator\\controller', '2021-04-08 17:38:55', '2021-04-08 17:44:26');
INSERT INTO `t_project_template` VALUES ('2', '1', '2', 'com.dwj.generator.service', 'D:\\my-code\\easy-generator\\src\\main\\java\\com\\dwj\\generator\\service', '2021-04-08 17:38:55', '2021-04-08 17:44:27');
INSERT INTO `t_project_template` VALUES ('3', '1', '3', 'com.dwj.generator.service.impl', 'D:\\my-code\\easy-generator\\src\\main\\java\\com\\dwj\\generator\\service\\impl', '2021-04-08 17:38:55', '2021-04-08 17:44:28');
INSERT INTO `t_project_template` VALUES ('4', '1', '4', 'com.dwj.generator.dao.mapper', 'D:\\my-code\\easy-generator\\src\\main\\java\\com\\dwj\\generator\\dao\\mapper', '2021-04-08 17:38:55', '2021-04-08 17:44:30');
INSERT INTO `t_project_template` VALUES ('5', '1', '5', 'com.dwj.generator.dao.entity', 'D:\\my-code\\easy-generator\\src\\main\\java\\com\\dwj\\generator\\dao\\entity', '2021-04-08 17:38:55', '2021-04-08 17:44:32');
INSERT INTO `t_project_template` VALUES ('6', '1', '6', '', 'D:\\my-code\\easy-generator\\src\\main\\resources\\mapper', '2021-04-08 17:38:55', '2021-04-08 17:44:37');

-- ----------------------------
-- Table structure for t_template
-- ----------------------------
DROP TABLE IF EXISTS `t_template`;
CREATE TABLE `t_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
  `template_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板类型',
  `template_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '模板内容',
  `file_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成文件的前缀',
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成文件的后缀',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码模配置板表';

-- ----------------------------
-- Records of t_template
-- ----------------------------
INSERT INTO `t_template` VALUES ('1', '基础Controller', 'CONTROLLER', 'package ${controllerPackage!};\n\nimport org.springframework.web.bind.annotation.RequestMapping;\n<#if restControllerStyle>\nimport org.springframework.web.bind.annotation.RestController;\n<#else>\nimport org.springframework.stereotype.Controller;\n</#if>\n\n/**\n * ${table.comment!} 前端控制器\n *\n * @author ${author}\n * @since ${date}\n */\n<#if restControllerStyle>\n@RestController\n<#else>\n@Controller\n</#if>\n@RequestMapping(\"/\")\npublic class ${controllerClassName!} {\n\n}\n', '', 'Controller', '基础Controller模板', '2021-04-08 17:12:44', '2021-04-08 17:14:33');
INSERT INTO `t_template` VALUES ('2', '基础Service', 'SERVICE', 'package ${servicePackage!};\n\n/**\n * ${table.comment!} 服务类\n *\n * @author ${author}\n * @since ${date}\n */\npublic interface ${serviceClassName!} {\n\n}\n', 'I', 'Service', '基础Service模板', '2021-04-08 17:14:16', '2021-04-08 17:41:03');
INSERT INTO `t_template` VALUES ('3', '基础ServiceImpl', 'SERVICE_IMPL', 'package ${serviceImplPackage};\n\nimport ${servicePackage}.${serviceClassName};\nimport org.springframework.stereotype.Service;\n\n/**\n * ${table.comment!} 服务实现类\n *\n * @author ${author}\n * @since ${date}\n */\n@Service\npublic class ${serviceImplClassName} implements ${serviceClassName} {\n\n}\n', '', 'ServiceImpl', '基础ServiceImpl模板', '2021-04-08 17:15:53', '2021-04-08 17:15:53');
INSERT INTO `t_template` VALUES ('4', '基础Mapper', 'MAPPER', 'package ${mapperPackage};\n\n/**\n * ${table.comment!} Mapper 接口\n *\n * @author ${author}\n * @since ${date}\n */\npublic interface ${mapperClassName} {\n\n}\n', '', 'Mapper', '基础Mapper模板', '2021-04-08 17:16:49', '2021-04-08 17:16:49');
INSERT INTO `t_template` VALUES ('5', '基础Entity', 'ENTITY', 'package ${entityPackage};\n\nimport java.io.Serializable;\n\n<#list table.fields as field>\n	<#if field.columnType?? && field.columnType.pkg??>\nimport ${field.columnType.pkg};\n    </#if>\n</#list>\n\n<#--获取主键类型-->\n<#list table.fields as field>\n    <#if field.keyFlag>\n        <#assign keyPropertyType= field.propertyType />\n    </#if>\n</#list>\n/**\n* ${table.comment!}${entityClassName}\n*\n* @author ${author}\n* @since ${date}\n*/\npublic class ${entityClassName} implements Serializable {\n	private static final long serialVersionUID = 1L;\n<#-- ----------  BEGIN 字段循环遍历  ---------->\n<#list table.fields as field>\n    <#if field.keyFlag>\n        <#assign keyPropertyName=\"${field.propertyName}\"/>\n    </#if>\n    <#if field.comment!?length gt 0>\n    /**\n    * ${field.comment}\n    */\n    </#if>\n    <#if !field.keyFlag>\n    private ${field.propertyType} ${field.propertyName};\n    </#if>\n\n</#list>\n<#------------  END 字段循环遍历  ---------->\n<#--getter setter-->\n<#list table.fields as field>\n    <#if field.propertyType == \"boolean\">\n        <#assign getprefix=\"is\"/>\n    <#else>\n        <#assign getprefix=\"get\"/>\n    </#if>\n    <#if !field.keyFlag>\n    public ${field.propertyType} ${getprefix}${field.capitalName}() {\n        return ${field.propertyName};\n    }\n\n    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\n        this.${field.propertyName} = ${field.propertyName};\n    }\n\n    </#if>\n</#list>\n}\n', '', '', '基础Entity模板', '2021-04-08 17:18:44', '2021-04-08 17:42:31');
INSERT INTO `t_template` VALUES ('6', '基础MapperXml', 'MAPPER_XML', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"${mapperPackage}.${mapperClassName}\">\n\n<#if enableCache>\n    <!-- 开启二级缓存 -->\n    <cache type=\"org.mybatis.caches.ehcache.LoggingEhcache\"/>\n\n</#if>\n<#if baseResultMap?? && entityPackage??>\n    <!-- 通用查询映射结果 -->\n    <resultMap id=\"BaseResultMap\" type=\"${entityPackage}.${entityClassName}\">\n<#list table.fields as field>\n<#if field.keyFlag><#--生成主键排在第一位-->\n        <id column=\"${field.name}\" property=\"${field.propertyName}\" />\n</#if>\n</#list>\n<#list table.commonFields as field><#--生成公共字段 -->\n    <result column=\"${field.name}\" property=\"${field.propertyName}\" />\n</#list>\n<#list table.fields as field>\n<#if !field.keyFlag><#--生成普通字段 -->\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\n</#if>\n</#list>\n    </resultMap>\n\n</#if>\n<#if baseColumnList>\n    <!-- 通用查询结果列 -->\n    <sql id=\"Base_Column_List\">\n<#list table.commonFields as field>\n        ${field.name},\n</#list>\n        ${table.fieldNames}\n    </sql>\n\n</#if>\n</mapper>\n', '', 'Mapper', '基础MapperXml模板', '2021-04-08 17:19:34', '2021-04-08 17:19:34');
