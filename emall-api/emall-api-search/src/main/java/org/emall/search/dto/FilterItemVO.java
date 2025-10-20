package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 筛选条件项VO
 * 用于前端展示搜索结果中的筛选参数（如品牌、价格区间）
 */
@Data
public class FilterItemVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;           // 筛选项名称（如"华为"、"1000-2000"）
    private Integer count;         // 该筛选项下的商品数量
    private Boolean checked;       // 是否被选中（前端回显用）
    private String value;          // 筛选值（如品牌ID、价格区间最小值）
}