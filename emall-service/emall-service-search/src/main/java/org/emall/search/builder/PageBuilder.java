package org.emall.search.builder;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.emall.search.context.SearchProductPageContext;
import org.emall.search.doc.ProductDoc;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Li Rui
 * @date 2025-11-11
 */
public class PageBuilder {

    public static <T> IPage<T> buildPage(SearchProductPageContext context, int pageNo, int pageSize, SearchResponse<ProductDoc> validatedResponse, BiFunction<ProductDoc, SearchProductPageContext, T> converter) {
        long total = 0;
        List<T> records = validatedResponse.hits().hits().stream().map(hit -> converter.apply(hit.source(), context)).collect(Collectors.toList());
        if (validatedResponse.hits().total() != null) {
            total = validatedResponse.hits().total().value();
        }
        IPage<T> page = new Page<>(pageNo, pageSize, total);
        page.setRecords(records);
        return page;
    }

    public static <T> IPage<T> emptyPage(int pageNo, int pageSize) {
        IPage<T> page = new Page<>(pageNo, pageSize, 0);
        page.setRecords(Lists.newArrayList());
        return page;
    }
}