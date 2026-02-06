package org.emall.search.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品列表项VO
 * 用于前端展示商品搜索结果中的单个商品信息
 */
@Data
public class ProductSearchPageVO implements Serializable {
    private Long id;
    private String productName;
    private String brandName;
    private BigDecimal price;
    private String mainImage;
    private Integer sales;
    private BigDecimal score;
    private Long categoryId;
    private Boolean hasStock;
}