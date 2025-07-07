CREATE TABLE `t_user`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`        varchar(64)  NOT NULL COMMENT '用户名',
    `password`        varchar(128) NOT NULL COMMENT '加密后的密码',
    `nickname`        varchar(64)  DEFAULT NULL COMMENT '昵称',
    `phone`           varchar(20)  DEFAULT NULL COMMENT '手机号',
    `email`           varchar(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`          varchar(500) DEFAULT NULL COMMENT '头像URL',
    `gender`          tinyint(1) DEFAULT '0' COMMENT '性别:0-未知,1-男,2-女',
    `birthday`        date         DEFAULT NULL COMMENT '生日',
    `source_type`     tinyint(1) DEFAULT '0' COMMENT '注册来源:0-PC,1-APP,2-小程序,3-H5',
    `status`          tinyint(1) DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
    `user_level`      tinyint(2) DEFAULT '0' COMMENT '用户等级',
    `last_login_time` datetime     DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`   varchar(50)  DEFAULT NULL COMMENT '最后登录IP',
    `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time`     datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY               `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';