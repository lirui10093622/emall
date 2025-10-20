package org.emall.search.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.emall.common.enums.ApiResult;
import org.emall.common.exception.EmallException;
import org.emall.search.constants.ElasticConstant;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.*;
import org.emall.search.mapper.SearchHotWordMapper;
import org.emall.search.model.SearchHotWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索核心服务
 */
@Slf4j
@Service
public class SearchCoreService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private SearchHotWordMapper searchHotWordMapper;

    public boolean sync(List<ProductDoc> productDocList) {
        elasticsearchRestTemplate.save(productDocList);
        return true;
    }

    public List<ProductDoc> search(String keyword) {
        try {
            SearchRequest searchRequest = new SearchRequest(ElasticConstant.PRODUCT_INDEX);
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.multiMatchQuery(keyword, "name", "description", "brandName", "categoryName").boost(3.0f));

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<ProductDoc> productList = Arrays.stream(response.getHits().getHits())
                    .map(this::convertToProductDoc)
                    .collect(Collectors.toList());
            return productList;
        } catch (Exception e) {
            throw new EmallException(ApiResult.FAIL.getCode(), "搜索服务异常", e);
        }
    }

    public SearchProductResult searchProduct(SearchProductParam param) {
        if (param == null || StringUtils.isEmpty(param.getKeyword())) {
            throw new EmallException(ApiResult.FAIL.getCode(), "搜索关键词不能为空");
        }
        try {
            SearchRequest searchRequest = new SearchRequest(ElasticConstant.PRODUCT_INDEX);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.multiMatchQuery(param.getKeyword(),
                    "productName", "description", "brandName").boost(3.0f));

            if (param.getCategoryId() != null) {
                boolQuery.filter(QueryBuilders.termQuery("categoryId", param.getCategoryId()));
            }

            if (param.getPriceMin() != null) {
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(param.getPriceMin()));
            }
            if (param.getPriceMax() != null) {
                boolQuery.filter(QueryBuilders.rangeQuery("price").lte(param.getPriceMax()));
            }

            sourceBuilder.query(boolQuery);
            searchRequest.source(sourceBuilder);

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<ProductVO> productList = Arrays.stream(response.getHits().getHits())
                    .map(this::convertToProductVO)
                    .collect(Collectors.toList());

            SearchProductResult result = new SearchProductResult();
            result.setTotal(response.getHits().getTotalHits().value);
            result.setProductList(productList);
            return result;
        } catch (IOException e) {
            log.error("ES搜索失败", e);
            throw new EmallException(ApiResult.FAIL.getCode(), "搜索服务异常");
        }
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