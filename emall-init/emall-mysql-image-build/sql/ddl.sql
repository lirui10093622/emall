/*!40101 SET NAMES utf8 */;

CREATE DATABASE IF NOT EXISTS `cart-db`;
USE `cart-db`;

CREATE DATABASE IF NOT EXISTS `order-db`;
USE `order-db`;

CREATE DATABASE IF NOT EXISTS `pay-db`;
USE `pay-db`;

CREATE DATABASE IF NOT EXISTS `product-db`;
USE `product-db`;

-- 商品表
CREATE TABLE `t_product` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
`product_name` varchar(255) NOT NULL COMMENT '商品名称',
`product_code` varchar(64) NOT NULL COMMENT '商品编码',
`category_id` bigint(20) NOT NULL COMMENT '分类ID',
`brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
`price` decimal(10,2) NOT NULL COMMENT '商品售价',
`market_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
`status` tinyint(1) NOT NULL COMMENT '商品状态（0-下架/1-上架/2-售罄）',
`description` text COMMENT '商品描述',
`main_image` varchar(255) NOT NULL COMMENT '主图URL',
`create_user` varchar(64) NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_user` varchar(64) NOT NULL COMMENT '更新人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0-正常/1-删除）',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_product_code` (`product_code`),
KEY `idx_category_id` (`category_id`),
KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品分类表
CREATE TABLE `t_product_category` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
`category_name` varchar(64) NOT NULL COMMENT '分类名称',
`parent_id` bigint(20) NOT NULL COMMENT '父分类ID',
`level` int(2) NOT NULL COMMENT '分类级别',
`sort` int(4) DEFAULT '0' COMMENT '排序值',
`status` tinyint(1) DEFAULT '1' COMMENT '状态（0-禁用/1-启用）',
`create_user` varchar(64) NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_user` varchar(64) NOT NULL COMMENT '更新人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
PRIMARY KEY (`id`),
KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品SKU表
CREATE TABLE `t_product_sku` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
 `product_id` bigint(20) NOT NULL COMMENT '商品ID',
 `sku_code` varchar(64) NOT NULL COMMENT 'SKU编码',
 `spec_json` varchar(512) NOT NULL COMMENT '规格JSON',
 `price` decimal(10,2) NOT NULL COMMENT 'SKU售价',
 `stock` int(10) NOT NULL COMMENT '库存数量',
 `image` varchar(255) DEFAULT NULL COMMENT 'SKU图片',
 `create_user` varchar(64) NOT NULL COMMENT '创建人',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `update_user` varchar(64) NOT NULL COMMENT '更新人',
 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
 PRIMARY KEY (`id`),
 UNIQUE KEY `uk_sku_code` (`sku_code`),
 KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 库存表
CREATE TABLE `t_product_inventory` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
`sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
`total_stock` int(10) NOT NULL COMMENT '总库存',
`locked_stock` int(10) DEFAULT '0' COMMENT '锁定库存',
`available_stock` int(10) NOT NULL COMMENT '可用库存',
`version` int(10) DEFAULT '0' COMMENT '乐观锁版本',
`create_user` varchar(64) NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_user` varchar(64) NOT NULL COMMENT '更新人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
PRIMARY KEY (`id`),
KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 商品属性表
CREATE TABLE `t_product_attribute` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
`product_id` bigint(20) NOT NULL COMMENT '商品ID',
`attribute_name` varchar(64) NOT NULL COMMENT '属性名称',
`attribute_value` varchar(255) NOT NULL COMMENT '属性值',
`type` tinyint(1) NOT NULL COMMENT '属性类型（0-通用/1-规格）',
`create_user` varchar(64) NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_user` varchar(64) NOT NULL COMMENT '更新人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
PRIMARY KEY (`id`),
KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- 商品图片表
CREATE TABLE `t_product_image` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
`product_id` bigint(20) NOT NULL COMMENT '商品ID',
`image_url` varchar(255) NOT NULL COMMENT '图片URL',
`sort` int(4) DEFAULT '0' COMMENT '排序值',
`type` tinyint(1) DEFAULT '1' COMMENT '图片类型（1-商品图/2-详情图）',
`create_user` varchar(64) NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_user` varchar(64) NOT NULL COMMENT '更新人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
PRIMARY KEY (`id`),
KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

CREATE DATABASE IF NOT EXISTS `search-db`;
USE `search-db`;

DROP TABLE IF EXISTS `t_search_history`;
CREATE TABLE `t_search_history` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_id` bigint(20) DEFAULT NULL COMMENT '用户ID（未登录为NULL）',
`keyword` varchar(255) NOT NULL COMMENT '搜索关键词',
`search_time` datetime NOT NULL COMMENT '搜索时间',
`ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
`device_type` varchar(20) DEFAULT NULL COMMENT '设备类型（PC/APP/WAP）',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`create_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '创建人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`update_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '更新人',
PRIMARY KEY (`id`),
KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
KEY `idx_search_time` (`search_time`) COMMENT '搜索时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户搜索历史记录表';

DROP TABLE IF EXISTS `t_search_hot_word`;
CREATE TABLE `t_search_hot_word` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`keyword` varchar(255) NOT NULL COMMENT '关键词',
`category_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '分类ID（0表示全平台）',
`search_count` int(11) NOT NULL DEFAULT 0 COMMENT '搜索次数',
`rank` int(11) DEFAULT NULL COMMENT '排名',
`period` varchar(10) NOT NULL DEFAULT 'day' COMMENT '统计周期（day/week/month）',
`date` date NOT NULL COMMENT '统计日期',
`status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（1-启用，0-禁用）',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`create_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '创建人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`update_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '更新人',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_keyword_date_period` (`keyword`,`date`,`period`) COMMENT '关键词+日期+周期唯一索引',
KEY `idx_category_date` (`category_id`,`date`) COMMENT '分类+日期索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门搜索词统计表';

DROP TABLE IF EXISTS `t_search_synonym`;
CREATE TABLE `t_search_synonym` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`word_group` varchar(500) NOT NULL COMMENT '同义词组（逗号分隔，如"手机,智能机,移动电话"）',
`status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（1-启用，0-禁用）',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`create_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '创建人',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`update_user` varchar(50) NOT NULL DEFAULT 'SYSTEM' COMMENT '更新人',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_word_group` (`word_group`) COMMENT '同义词组唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索同义词管理表';


CREATE DATABASE IF NOT EXISTS `sms-db`;
USE `sms-db`;


-- user-db
CREATE DATABASE IF NOT EXISTS `user-db`;
USE `user-db`;

CREATE TABLE IF NOT EXISTS `t_user`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `name` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '加密后的密码',
    `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
    `gender` tinyint(1) DEFAULT '0' COMMENT '性别:0-未知,1-男,2-女',
    `birthday` date DEFAULT NULL COMMENT '生日',
    `source_type` tinyint(1) DEFAULT '0' COMMENT '注册来源:0-PC,1-APP,2-小程序,3-H5',
    `register_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `status` tinyint(1) DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
    `user_level` tinyint DEFAULT '0' COMMENT '用户等级',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_user` varchar(20) DEFAULT '' COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(20) DEFAULT '' COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_create_time` (`create_time`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户主表';

CREATE TABLE IF NOT EXISTS `t_role`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` varchar(50) NOT NULL COMMENT '角色名',
    `desc` varchar(500) DEFAULT NULL COMMENT '描述',
    `create_user` varchar(20) DEFAULT '' COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(20) DEFAULT '' COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `t_permission`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name` varchar(50) NOT NULL COMMENT '权限名',
    `path` varchar(500) NOT NULL COMMENT 'Ant路径模式',
    `operation` varchar(10) NOT NULL COMMENT '操作类型: ALL, GET, POST, PUT, DELETE',
    `desc` varchar(500) DEFAULT NULL COMMENT '描述',
    `create_user` varchar(20) DEFAULT '' COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(20) DEFAULT '' COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';

CREATE TABLE IF NOT EXISTS `t_role_permission`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `permission_id` bigint NOT NULL COMMENT '权限ID',
    `create_user` varchar(20) DEFAULT '' COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(20) DEFAULT '' COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_id_permission_id` (`role_id`,`permission_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限表';

CREATE TABLE IF NOT EXISTS `t_user_role`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `create_user` varchar(20) DEFAULT '' COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(20) DEFAULT '' COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id_role_id` (`user_id`,`role_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';