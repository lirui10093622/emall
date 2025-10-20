package org.emall.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索推荐结果模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestResult implements Serializable {

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 推荐词列表
     */
    private List<String> suggestions;

    /**
     * 推荐类型（可选：keyword-关键词/brand-品牌/category-分类）
     */
    private String suggestType;

    /**
     * 相关商品数量（可选）
     */
    private Integer productCount;
}