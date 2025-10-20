package org.emall.search.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SearchProductParam implements Serializable {
    private Long userId;
    private String keyword;
    private Long categoryId;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String brandIds;
    private String sortField;
    private String sortOrder;
    private Integer pageNum = 1;
    private Integer pageSize = 20;
    private Boolean hasStock;
    private String props;
}