package org.emall.search.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 热门搜索词实体类
 * 对应数据库表：t_search_hot_word
 */
@Data
@TableName("t_search_hot_word")
public class SearchHotWord {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 分类ID（0表示全平台）
     */
    private Long categoryId;

    /**
     * 搜索次数
     */
    private Integer searchCount;

    /**
     * 排名（每日更新）
     */
    private Integer rank;

    /**
     * 统计周期（day-日/week-周/month-月）
     */
    private String period;

    /**
     * 统计日期
     */
    private LocalDate date;

    /**
     * 状态（1-启用，0-禁用）
     */
    private Integer status = 1;

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