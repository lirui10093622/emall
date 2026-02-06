package org.emall.search.service.support;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.emall.search.config.ElasticsearchConfig;
import org.emall.search.dto.ElasticSearchPageParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.emall.search.builder.PageBuilder.buildPage;
import static org.emall.search.builder.PageBuilder.emptyPage;
import static org.emall.search.utils.ElasticSearchResponseValidateUtils.queryCountSuccess;
import static org.emall.search.utils.ElasticSearchResponseValidateUtils.queryPageSuccess;

/**
 * @author Li Rui
 * @date 2026-02-06
 */
@Component
@Slf4j
public class ElasticSearchQuerySupport {
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public <T, TDocument> IPage<T> searchPage(ElasticSearchPageParamDTO<T, TDocument> param) {
        long start = System.currentTimeMillis();
        IPage<T> pageInfo = null;
        try {
            log.info("searchPage, param: {}", param);
            SearchRequest.Builder requestBuilder = new SearchRequest.Builder();
            requestBuilder.index(param.getIndex());

            BoolQuery.Builder queryBuilder = new BoolQuery.Builder();
            if (CollectionUtils.isNotEmpty(param.getFilters())) {
                queryBuilder.filter(param.getFilters());
            }
            if (CollectionUtils.isNotEmpty(param.getQueries())) {
                queryBuilder.must(param.getQueries());
            }
            int from = (param.getPageNo() - 1) * param.getPageSize();

            requestBuilder.query(queryBuilder.build()._toQuery());
            requestBuilder.from(from).size(param.getPageSize());
            requestBuilder.sort(param.getSort());

            SearchRequest request = requestBuilder.build();
            SearchResponse<TDocument> response = elasticsearchClient.search(request, param.getTDocumentClass());
            log.info("searchPage, request: {}, response: {}", request, response);
            if (queryPageSuccess(response)) {
                pageInfo = buildPage(response, param);
                if (pageInfo.getTotal() >= elasticsearchConfig.getMaxResultWindow()) {
                    pageInfo.setTotal(searchCount(param));
                }
            } else {
                log.warn("searchPage error, bad response: {}", response);
                pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
            }
        } catch (ElasticsearchException e) {
            log.warn("searchPage error, status: {}, endpointId: {}, response: {}, error: {}", e.status(), e.endpointId(), e.response(), e.error());
            log.warn("searchPage error", e);
            pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
        } catch (Exception e) {
            log.warn("searchPage error", e);
            pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
        } finally {
            log.debug("searchPage, time used: {}ms, param: {}, pageInfo: {}", (System.currentTimeMillis() - start), param, pageInfo);
        }
        return pageInfo;
    }

    private <T, TDocument> long searchCount(ElasticSearchPageParamDTO<T, TDocument> param) {
        long start = System.currentTimeMillis();
        long count = 0;
        try {
            log.info("searchCount, param: {}", param);
            CountRequest.Builder requestBuilder = new CountRequest.Builder();
            requestBuilder.index(param.getIndex());

            requestBuilder.query(q -> q.bool(b -> b
                    .filter(param.getFilters())
                    .must(param.getQueries())
            ));
            CountRequest request = requestBuilder.build();
            CountResponse response = elasticsearchClient.count(request);
            log.info("searchCount, request: {}, response: {}", request, response);
            if (queryCountSuccess(response)) {
                count = response.count();
            } else {
                log.warn("searchCount error, bad response[is null]");
            }
        } catch (ElasticsearchException e) {
            log.warn("searchCount error, status: {}, endpointId: {}, response: {}, error: {}", e.status(), e.endpointId(), e.response(), e.error());
            log.warn("searchCount error", e);
        } catch (Exception e) {
            log.warn("searchCount error", e);
        } finally {
            log.debug("searchCount, time used: {}ms, count: {}", (System.currentTimeMillis() - start), count);
        }
        return count;
    }
}