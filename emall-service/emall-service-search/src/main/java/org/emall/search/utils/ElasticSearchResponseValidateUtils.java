package org.emall.search.utils;

import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;

/**
 * @author Li Rui
 * @date 2026-02-06
 */
public class ElasticSearchResponseValidateUtils {

    public static <TDocument> boolean queryPageSuccess(SearchResponse<TDocument> response) {
        return response != null && response.hits() != null && response.hits().hits() != null;
    }

    public static boolean queryCountSuccess(CountResponse response) {
        return response != null;
    }
}