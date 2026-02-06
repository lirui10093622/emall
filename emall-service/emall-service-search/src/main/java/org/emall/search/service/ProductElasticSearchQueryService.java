package org.emall.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.search.SearchHit;
import org.emall.common.enums.ApiResult;
import org.emall.common.exception.EmallException;
import org.emall.search.builder.product.ProductQueryBuilder;
import org.emall.search.builder.product.ProductSortOptionsBuilder;
import org.emall.search.config.ElasticsearchConfig;
import org.emall.search.context.SearchProductPageContext;
import org.emall.search.converter.SearchProductConverter;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.SearchHotParam;
import org.emall.search.dto.SearchHotResult;
import org.emall.search.param.SearchProductPageParam;
import org.emall.search.mapper.SearchHotWordMapper;
import org.emall.search.model.SearchHotWord;
import org.emall.search.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.emall.search.builder.PageBuilder.buildPage;
import static org.emall.search.builder.PageBuilder.emptyPage;

/**
 * 产品搜索服务
 */
@Slf4j
@Service
public class ProductElasticSearchQueryService {
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private SearchHotWordMapper searchHotWordMapper;
    @Autowired
    private SearchProductConverter searchProductConverter;

    public IPage<ProductVO> searchProductPage(SearchProductPageParam param) {
        long start = System.currentTimeMillis();
        IPage<ProductVO> pageInfo = null;
        try {
            log.info("searchProductPage, param: {}", param);
            SearchRequest.Builder requestBuilder = new SearchRequest.Builder();
            requestBuilder.index(elasticsearchConfig.getIndexName());

            List<Query> filters = ProductQueryBuilder.filters(param);
            List<Query> queries = ProductQueryBuilder.queries(param);

            BoolQuery.Builder queryBuilder = new BoolQuery.Builder();
            queryBuilder.filter(filters);
            queryBuilder.must(queries);

            List<SortOptions> sort = ProductSortOptionsBuilder.sorters(param);
            int from = (param.getPageNo() - 1) * param.getPageSize();

            requestBuilder.query(queryBuilder.build()._toQuery());
            requestBuilder.sort(sort);
            requestBuilder.from(from).size(param.getPageSize());

            SearchRequest request = requestBuilder.build();
            SearchResponse<ProductDoc> response = elasticsearchClient.search(request, ProductDoc.class);

            if (response != null && response.hits() != null && response.hits().hits() != null) {
                pageInfo = buildPage(new SearchProductPageContext(), param.getPageNo(), param.getPageSize(), response, searchProductConverter);
                if (pageInfo.getTotal() >= elasticsearchConfig.getMaxResultWindow()) {
                    pageInfo.setTotal(searchCount(filters, queries));
                }
            } else {
                log.warn("searchProductPage error, bad response: {}", response);
                pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
            }
        } catch (ElasticsearchException e) {
            log.warn("searchProductPage error, status: {}, endpointId: {}, response: {}, error: {}", e.status(), e.endpointId(), e.response(), e.error());
            log.warn("searchProductPage error", e);
            pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
        } catch (Exception e) {
            log.warn("searchProductPage error", e);
            pageInfo = emptyPage(param.getPageNo(), param.getPageSize());
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("searchProductPage, time used: {}ms, pageInfo: {}", (System.currentTimeMillis() - start), pageInfo);
            }
        }
        return pageInfo;
    }

    public long searchProductCount(SearchProductPageParam param) {
        List<Query> filters = ProductQueryBuilder.filters(param);
        List<Query> queries = ProductQueryBuilder.queries(param);
        return searchCount(filters, queries);
    }

    private long searchCount(List<Query> filters, List<Query> queries) {
        long start = System.currentTimeMillis();
        long count = 0;
        try {
            if (log.isDebugEnabled()) {
                log.debug("searchCount, filters: {}", filters);
            }

            CountRequest.Builder requestBuilder = new CountRequest.Builder();
            requestBuilder.index(elasticsearchConfig.getIndexName());
            requestBuilder.query(q -> q.bool(b -> b
                    .filter(filters)
                    .must(queries)
            ));
            CountRequest request = requestBuilder.build();
            CountResponse response = elasticsearchClient.count(request);
            if (log.isDebugEnabled()) {
                log.debug("searchCount, response: {}", response);
            }
            if (response != null) {
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
            if (log.isDebugEnabled()) {
                log.debug("searchCount, time used: {}ms, count: {}", (System.currentTimeMillis() - start), count);
            }
        }
        return count;
    }

    public SearchHotResult getHotWords(SearchHotParam param) {
        // 1. 参数处理
        if (param == null) {
            param = new SearchHotParam();
        }
        if (param.getSize() == null || param.getSize() < 1 || param.getSize() > 50) {
            param.setSize(10);
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(param.getPeriod())) {
            param.setPeriod("day"); // 默认按天统计
        }
        if (param.getDate() == null) {
            param.setDate(LocalDate.now());
        }

        // 2. 查询热门词（调用Mapper）
        List<SearchHotWord> hotWords = searchHotWordMapper.selectHotWords(
                param.getCategoryId(),
                param.getPeriod(),
                param.getDate(),
                param.getSize()
        );

        // 3. 转换结果VO
        SearchHotResult result = new SearchHotResult();
        result.setHotWords(hotWords.stream().map(hotWord -> {
            SearchHotResult.SearchHotWordVO vo = new SearchHotResult.SearchHotWordVO();
            vo.setKeyword(hotWord.getKeyword());
            vo.setSearchCount(hotWord.getSearchCount());
            vo.setRank(hotWord.getRank());
            return vo;
        }).collect(Collectors.toList()));
        return result;
    }

    private ProductDoc convertToProductDoc(SearchHit hit) {
        Map<String, Object> source = hit.getSourceAsMap();
        ProductDoc doc = new ProductDoc();
        doc.setId(Long.valueOf(source.get("id").toString()));
        doc.setName(source.get("name").toString());
        doc.setCode(source.get("code").toString());
        doc.setCategoryId(Long.valueOf(source.get("categoryId").toString()));
        doc.setCategoryName(source.get("categoryName").toString());
        return doc;
    }

    private ProductVO convertToProductVO(SearchHit hit) {
        Map<String, Object> source = hit.getSourceAsMap();
        ProductVO vo = new ProductVO();
        vo.setId(Long.valueOf(source.get("id").toString()));
        vo.setProductName(source.get("productName").toString());
        vo.setBrandName(source.get("brandName").toString());
        vo.setPrice(new BigDecimal(source.get("price").toString()));
        vo.setMainImage(source.get("mainImage").toString());
        return vo;
    }
}