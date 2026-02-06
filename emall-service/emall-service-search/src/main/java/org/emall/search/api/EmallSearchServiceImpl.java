package org.emall.search.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.api.dto.HealthDto;
import org.emall.common.enums.AppStatusEnum;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.param.SearchProductPageParam;
import org.emall.search.service.ProductElasticSearchQueryService;
import org.emall.search.service.SearchHistoryService;
import org.emall.search.service.SearchSuggestService;
import org.emall.search.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Slf4j
@DubboService
public class EmallSearchServiceImpl implements EmallSearchService {
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private ProductElasticSearchQueryService searchProductService;
    @Autowired
    private SearchHistoryService searchHistoryService;
    @Autowired
    private SearchSuggestService searchSuggestService;

    @Override
    public EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success(new HealthDto(appName, AppStatusEnum.RUNNING.name()));
    }

    @Override
    public EmallResponse<IPage<ProductVO>> searchProductPage(EmallRequest<SearchProductPageParam> request) throws EmallException {
        return EmallResponse.success(searchProductService.search(request.getData()));
    }
}
