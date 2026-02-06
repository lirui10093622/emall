package org.emall.search.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索结果类
 * 包含商品列表、总记录数、筛选参数等信息
 */
@Data
public class SearchProductResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long total;                      // 总记录数
    private Integer pages;                   // 总页数
    private List<ProductSearchPageVO> productList; // 商品列表
    private Map<String, List<FilterItemVO>> filterParams; // 筛选参数（如价格区间、品牌）
    private String correctionSuggestion;     // 搜索纠错建议（如"手机"纠正为"华为手机"）
}