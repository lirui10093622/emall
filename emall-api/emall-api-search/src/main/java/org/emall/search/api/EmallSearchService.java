package org.emall.search.api;

import org.emall.common.api.EmallService;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.*;

import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallSearchService extends EmallService {

    EmallResponse<Boolean> sync(EmallRequest<List<ProductDoc>> request);

    EmallResponse<List<ProductDoc>> search(EmallRequest<String> request) throws EmallException;

    EmallResponse<SearchProductResult> searchProducts(EmallRequest<SearchProductParam> request);

    EmallResponse<SearchHotResult> getHotWords(EmallRequest<SearchHotParam> request);

    EmallResponse<SearchHistoryResult> getSearchHistory(EmallRequest<SearchHistoryParam> request);

    EmallResponse<Boolean> deleteSearchHistory(EmallRequest<SearchHistoryParam> request);

    EmallResponse<SearchSuggestResult> getSearchSuggest(EmallRequest<SearchSuggestParam> request);
}