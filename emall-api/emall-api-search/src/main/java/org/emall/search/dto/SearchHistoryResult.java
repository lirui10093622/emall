package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 用户搜索历史结果类
 * 包含用户ID、历史关键词列表及总数
 */
@Data
public class SearchHistoryResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long userId;           // 用户ID（未登录用户为null）
    private List<String> historyList; // 搜索历史关键词列表（按时间倒序）
    private Integer total;         // 总记录数
}