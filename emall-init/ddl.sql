CREATE DATABASE IF NOT EXISTS `cart-db`;
USE `cart-db`;

CREATE DATABASE IF NOT EXISTS `order-db`;
USE `order-db`;

CREATE DATABASE IF NOT EXISTS `pay-db`;
USE `pay-db`;

CREATE DATABASE IF NOT EXISTS `product-db`;
USE `product-db`;

CREATE DATABASE IF NOT EXISTS `search-db`;
USE `search-db`;

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