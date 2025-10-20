package org.emall.search.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.api.dto.HealthDto;
import org.emall.common.enums.AppStatusEnum;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.*;
import org.emall.search.service.SearchCoreService;
import org.emall.search.service.SearchHistoryService;
import org.emall.search.service.SearchSuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

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
    private SearchCoreService searchCoreService;
    @Autowired
    private SearchHistoryService searchHistoryService;
    @Autowired
    private SearchSuggestService searchSuggestService;

    @Override
    public EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success(new HealthDto(appName, AppStatusEnum.RUNNING.name()));
    }

    @Override
    public EmallResponse<Boolean> sync(EmallRequest<List<ProductDoc>> request) {
        return EmallResponse.success(searchCoreService.sync(request.getData()));
    }

    @Override
    public EmallResponse<List<ProductDoc>> search(EmallRequest<String> request) throws EmallException {
        return EmallResponse.success(searchCoreService.search(request.getData()));
    }

    @Override
    public EmallResponse<SearchProductResult> searchProducts(EmallRequest<SearchProductParam> request) {
        return EmallResponse.success(searchCoreService.searchProduct(request.getData()));
    }

    @Override
    public EmallResponse<SearchHotResult> getHotWords(EmallRequest<SearchHotParam> request) {
        return EmallResponse.success(searchCoreService.getHotWords(request.getData()));
    }

    @Override
    public EmallResponse<SearchHistoryResult> getSearchHistory(EmallRequest<SearchHistoryParam> request) {
        SearchHistoryParam param = request.getData();
        return EmallResponse.success(searchHistoryService.getUserSearchHistory(param));
    }

    @Override
    public EmallResponse<Boolean> deleteSearchHistory(EmallRequest<SearchHistoryParam> request) {
        return EmallResponse.success(searchHistoryService.deleteSearchHistory(request.getData()));
    }

    @Override
    public EmallResponse<SearchSuggestResult> getSearchSuggest(EmallRequest<SearchSuggestParam> request) {
        SearchSuggestParam param = request.getData();
        return EmallResponse.success(searchSuggestService.getSuggestions(param.getKeyword(), param.getSize()));
    }
}
