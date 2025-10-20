package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 热门搜索词结果类
 * 包含热门词列表及相关统计信息
 */
@Data
public class SearchHotResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<SearchHotWordVO> hotWords; // 热门词列表

    /**
     * 热门词VO类
     */
    @Data
    public static class SearchHotWordVO implements Serializable {
        private String keyword;       // 关键词
        private Integer searchCount;     // 搜索次数
        private Integer rank;      // 排名（1为最高）
        private Long categoryId;   // 分类ID（可选）
    }
}