package org.emall.search.builder.product;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.emall.search.enums.KeywordOperatorEnum;
import org.emall.search.param.SearchProductPageParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.emall.search.builder.BaseQueryBuilder.*;

/**
 * @author Li Rui
 * @date 2026-01-29
 */
public class ProductQueryBuilder {

    public static List<Query> filters(SearchProductPageParam param) {
        List<Query> filters = new ArrayList<>();

        if (Objects.nonNull(param.getUserId())) {
            filters.add(equal("userId", param.getUserId()));
        }
        if (Objects.nonNull(param.getPriceMin())) {
            filters.add(gte("price", param.getPriceMin()));
        }
        if (Objects.nonNull(param.getPriceMax())) {
            filters.add(lte("price", param.getPriceMax()));
        }
        if (CollectionUtils.isNotEmpty(param.getCategoryIds())) {
            filters.add(inLongList("categoryId", param.getCategoryIds()));
        }
        if (CollectionUtils.isNotEmpty(param.getBrandIds())) {
            filters.add(inLongList("brandId", param.getBrandIds()));
        }
        return filters;
    }

    public static List<Query> queries(SearchProductPageParam param) {
        if (param.isKeywordSegmented()) {
            return buildKeywordQueries(true, param.getKeywordOperator(), param.getKeywords());
        }
        return Lists.newArrayList();
    }

    public static List<Query> buildKeywordQueries(Boolean keywordSegmented, KeywordOperatorEnum keywordOperator, List<String> keywords) {
        List<Query> queries = Lists.newArrayList();
        if (CollectionUtils.isEmpty(keywords)) {
            return queries;
        }

        List<Query> keywordQueryList = Lists.newArrayList();
        for (String keyword : keywords) {
            if (StringUtils.isNotBlank(keyword)) {
                keywordQueryList.add(buildKeywordQuery(keywordSegmented, keyword));
            }
        }
        if (CollectionUtils.isNotEmpty(keywordQueryList)) {
            if (Objects.equals(keywordOperator, KeywordOperatorEnum.OR)) {
                queries.add(or(keywordQueryList));
            } else {
                queries.add(and(keywordQueryList));
            }
        }
        return queries;
    }

    private static Query buildKeywordQuery(boolean keywordMatchQuery, String keyword) {
        String pattern = "*" + keyword + "*";
        if (keywordMatchQuery) {
            return or(WildcardQuery.of(m -> m.field("applicationCode").value(pattern).boost(15.0f))._toQuery(),
                    WildcardQuery.of(m -> m.field("name").value(pattern).boost(15.0f))._toQuery(),
                    nestedLike("intelligentRecommendedJobs", "intelligentRecommendedJobs.jobTitle", pattern, 15.0f),
                    WildcardQuery.of(m -> m.field("phone").value(pattern).boost(3.0f))._toQuery(),
                    WildcardQuery.of(m -> m.field("email").value(pattern).boost(3.0f))._toQuery(),
                    WildcardQuery.of(m -> m.field("lockJobName").value(pattern).boost(3.0f))._toQuery(),
                    MatchQuery.of(m -> m.field("keyWords").query(keyword).boost(1.0f))._toQuery()
            );
        } else {
            return or(like("applicationCode", pattern),
                    like("name", pattern),
                    nestedLike("intelligentRecommendedJobs", "intelligentRecommendedJobs.jobTitle", pattern),
                    like("phone", pattern),
                    like("email", pattern),
                    like("lockJobName", pattern),
                    like("experienceNames", pattern),
                    like("educationNames", pattern),
                    like("projectNames", pattern)
            );
        }
    }
}