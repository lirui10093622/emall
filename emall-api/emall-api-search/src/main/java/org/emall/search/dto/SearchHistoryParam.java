package org.emall.search.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户搜索历史请求参数类
 * 用于查询/删除用户搜索历史
 */
@Data
public class SearchHistoryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必传）
     */
    private Long userId;

    private Long id;

    /**
     * 搜索关键词（删除时必传）
     */
    private List<String> keywords;

    /**
     * 查询数量（默认20条）
     */
    private Integer size = 20;

    /**
     * 时间范围类型（可选：today-今日，week-本周，month-本月）
     */
    private String timeRange;
}