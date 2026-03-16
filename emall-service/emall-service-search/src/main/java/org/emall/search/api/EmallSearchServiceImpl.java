package org.emall.search.api;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.api.dto.HealthDto;
import org.emall.common.enums.AppStatusEnum;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.config.ElasticsearchConfig;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.ProductSearchPageParam;
import org.emall.search.service.ProductElasticSearchQueryService;
import org.emall.search.service.SearchHistoryService;
import org.emall.search.service.SearchSuggestService;
import org.emall.search.dto.ProductSearchPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
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
    private ProductElasticSearchQueryService searchProductService;
    @Autowired
    private SearchHistoryService searchHistoryService;
    @Autowired
    private SearchSuggestService searchSuggestService;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    @Override
    public EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success(new HealthDto(appName, AppStatusEnum.RUNNING.name()));
    }

    @Override
    public EmallResponse<IPage<ProductSearchPageVO>> searchProductPage(EmallRequest<ProductSearchPageParam> request) throws EmallException {
        return EmallResponse.success(searchProductService.searchPage(request.getData()));
    }

    @Override
    public EmallResponse<Boolean> sync(EmallRequest<List<ProductDoc>> request) throws EmallException {
        try {
            List<ProductDoc> products = request.getData();
            if (products == null || products.isEmpty()) {
                return EmallResponse.success(true);
            }

            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
            for (ProductDoc doc : products) {
                bulkBuilder.operations(op -> op
                        .index(idx -> idx
                                .index(elasticsearchConfig.getIndexName())
                                .id(String.valueOf(doc.getId()))
                                .document(doc)
                        )
                );
            }

            BulkResponse response = elasticsearchClient.bulk(bulkBuilder.build());
            if (response.errors()) {
                log.error("Bulk sync has errors:");
                for (BulkResponseItem item : response.items()) {
                    if (item.error() != null) {
                        log.error("Error for document ID {}: {}", item.id(), item.error().reason());
                    }
                }
                return EmallResponse.success(false);
            }
            log.info("Successfully synced {} products to Elasticsearch", products.size());
            return EmallResponse.success(true);
        } catch (Exception e) {
            log.error("Failed to sync products to Elasticsearch", e);
            throw new EmallException("SYNC_ERROR", "Failed to sync products", e);
        }
    }

    @Override
    public EmallResponse<List<ProductDoc>> search(EmallRequest<String> request) throws EmallException {
        try {
            String keyword = request.getData();
            if (keyword == null || keyword.trim().isEmpty()) {
                return EmallResponse.success(new ArrayList<>());
            }

            ProductSearchPageParam param = new ProductSearchPageParam();
            param.setPageNo(1);
            param.setPageSize(20);
            List<String> keywords = new ArrayList<>();
            keywords.add(keyword);
            param.setKeywords(keywords);

            IPage<ProductSearchPageVO> page = searchProductService.searchPage(param);
            List<ProductDoc> docs = new ArrayList<>();
            if (page != null && page.getRecords() != null) {
                for (ProductSearchPageVO vo : page.getRecords()) {
                    ProductDoc doc = new ProductDoc();
                    doc.setId(vo.getId());
                    doc.setName(vo.getProductName());
                    doc.setBrandName(vo.getBrandName());
                    docs.add(doc);
                }
            }
            return EmallResponse.success(docs);
        } catch (Exception e) {
            log.error("Failed to search products", e);
            throw new EmallException("SEARCH_ERROR", "Failed to search products", e);
        }
    }
}
