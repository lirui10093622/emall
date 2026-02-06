package org.emall.search.builder;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.emall.search.dto.ElasticSearchPageParamDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Li Rui
 * @date 2025-11-11
 */
public class PageBuilder {

    public static <T, TDocument> IPage<T> buildPage(SearchResponse<TDocument> validatedResponse, ElasticSearchPageParamDTO<T, TDocument> param) {
        long total = 0;
        List<T> records = validatedResponse.hits().hits().stream().map(hit -> param.getConverter().apply(hit.source(), param.getContext())).collect(Collectors.toList());
        if (validatedResponse.hits().total() != null) {
            total = validatedResponse.hits().total().value();
        }
        IPage<T> page = new Page<>(param.getPageNo(), param.getPageSize(), total);
        page.setRecords(records);
        return page;
    }

    public static <T> IPage<T> emptyPage(int pageNo, int pageSize) {
        IPage<T> page = new Page<>(pageNo, pageSize, 0);
        page.setRecords(Lists.newArrayList());
        return page;
    }
}