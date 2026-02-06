package org.emall.search.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.emall.common.dto.PageParam;
import org.emall.search.enums.KeywordOperatorEnum;
import org.emall.user.api.dto.LoginUser;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchProductPageParam extends PageParam {
    private LoginUser loginUser;
    private boolean keywordSegmented;
    private KeywordOperatorEnum keywordOperator;
    private List<String> keywords;
    private Long userId;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private List<Long> categoryIds;
    private List<Long> brandIds;
}