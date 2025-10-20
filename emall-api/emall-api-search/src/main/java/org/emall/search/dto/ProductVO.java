package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品列表项VO
 * 用于前端展示商品搜索结果中的单个商品信息
 */
@Data
public class ProductVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;              // 商品ID
    private String productName;    // 商品名称
    private String brandName;      // 品牌名称
    private BigDecimal price;      // 售价
    private String mainImage;      // 主图URL
    private Integer sales;         // 销量
    private BigDecimal score;      // 评分（5分制）
    private Long categoryId;       // 分类ID
    private Boolean hasStock;      // 是否有库存
}