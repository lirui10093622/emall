package org.emall.search.dto;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Data;
import org.emall.search.context.ElasticSearchContext;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Li Rui
 * @date 2026-02-06
 */
@Data
public class ElasticSearchPageParamDTO<T, TDocument> implements Serializable {
    private String index;
    private int pageNo;
    private int pageSize;
    private List<Query> filters;
    private List<Query> queries;
    private List<SortOptions> sort;
    private Class<TDocument> tDocumentClass;
    private BiFunction<TDocument, ElasticSearchContext, T> converter;
    private ElasticSearchContext context;
}