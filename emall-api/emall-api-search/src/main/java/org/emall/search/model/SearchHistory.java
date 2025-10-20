package org.emall.search.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户搜索历史实体类
 * 对应数据库表：t_search_history
 */
@Data
@TableName("t_search_history")
public class SearchHistory {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（未登录用户为null）
     */
    private Long userId;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 搜索时间
     */
    private LocalDateTime searchTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 设备类型（PC/APP/WAP）
     */
    private String deviceType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人（系统默认：SYSTEM）
     */
    private String createUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人（系统默认：SYSTEM）
     */
    private String updateUser;
}