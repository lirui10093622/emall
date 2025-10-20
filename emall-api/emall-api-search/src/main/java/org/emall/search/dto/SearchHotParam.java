package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 热门搜索词请求参数类
 * 用于查询热门搜索词列表
 */
@Data
public class SearchHotParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID（可选，0表示全平台）
     */
    private Long categoryId = 0L;

    /**
     * 获取数量（默认10条）
     */
    private Integer size = 10;

    /**
     * 统计周期（可选：day-日，week-周，month-月）
     */
    private String period = "day";

    /**
     * 是否需要排序（可选：true-按搜索次数排序，false-按自然顺序）
     */
    private Boolean sorted = true;

    private LocalDate date;
}