package org.emall.search.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.emall.search.builder.product.ProductQueryBuilder;
import org.emall.search.builder.product.ProductSortOptionsBuilder;
import org.emall.search.config.ElasticsearchConfig;
import org.emall.search.context.ElasticSearchContext;
import org.emall.search.converter.SearchProductConverter;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.ElasticSearchPageParamDTO;
import org.emall.search.dto.SearchHotParam;
import org.emall.search.dto.SearchHotResult;
import org.emall.search.mapper.SearchHotWordMapper;
import org.emall.search.model.SearchHotWord;
import org.emall.search.dto.ProductSearchPageParam;
import org.emall.search.service.support.ElasticSearchQuerySupport;
import org.emall.search.dto.ProductSearchPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品搜索服务
 */
@Slf4j
@Service
public class ProductElasticSearchQueryService {
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;
    @Autowired
    private SearchHotWordMapper searchHotWordMapper;
    @Autowired
    private SearchProductConverter searchProductConverter;
    @Autowired
    private ElasticSearchQuerySupport elasticSearchQuerySupport;

    public IPage<ProductSearchPageVO> searchPage(ProductSearchPageParam param) {
        ElasticSearchPageParamDTO<ProductSearchPageVO, ProductDoc> searchPageParamDTO = new ElasticSearchPageParamDTO<>();
        searchPageParamDTO.setIndex(elasticsearchConfig.getIndexName());
        searchPageParamDTO.setPageNo(param.getPageNo());
        searchPageParamDTO.setPageSize(param.getPageSize());
        searchPageParamDTO.setFilters(ProductQueryBuilder.filters(param));
        searchPageParamDTO.setQueries(ProductQueryBuilder.queries(param));
        searchPageParamDTO.setSort(ProductSortOptionsBuilder.sorters(param));
        searchPageParamDTO.setConverter(searchProductConverter);
        searchPageParamDTO.setContext(new ElasticSearchContext());
        return elasticSearchQuerySupport.searchPage(searchPageParamDTO);
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

    private ProductSearchPageVO convertToProductVO(SearchHit hit) {
        Map<String, Object> source = hit.getSourceAsMap();
        ProductSearchPageVO vo = new ProductSearchPageVO();
        vo.setId(Long.valueOf(source.get("id").toString()));
        vo.setProductName(source.get("productName").toString());
        vo.setBrandName(source.get("brandName").toString());
        vo.setPrice(new BigDecimal(source.get("price").toString()));
        vo.setMainImage(source.get("mainImage").toString());
        return vo;
    }
}