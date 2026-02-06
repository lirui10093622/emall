package org.emall.search.builder;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Li Rui
 * @date 2025-11-11
 */
public class BaseQueryBuilder {

    public static Query not(Query query) {
        return BoolQuery.of(m -> m.mustNot(query))._toQuery();
    }

    public static Query match(String fieldName, String value) {
        return MatchQuery.of(m -> m.field(fieldName).query(value))._toQuery();
    }

    public static Query equal(String fieldName, String value) {
        return TermQuery.of(m -> m.field(fieldName).value(value))._toQuery();
    }

    public static Query equal(String fieldName, long value) {
        return TermQuery.of(m -> m.field(fieldName).value(value))._toQuery();
    }

    public static Query nestedEqual(String path, String fieldName, String value) {
        return NestedQuery.of(n -> n.path(path).query(equal(fieldName, value)))._toQuery();
    }

    public static Query equal(String fieldName, boolean value) {
        return TermQuery.of(m -> m.field(fieldName).value(value))._toQuery();
    }

    public static Query anyEqual(List<String> fieldNames, List<Integer> values) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        for (String fieldName : fieldNames) {
            builder.should(inIntegerList(fieldName, values));
        }
        return builder.minimumShouldMatch("1").build()._toQuery();
    }

    public static <T> Query lt(String fieldName, T value) {
        return RangeQuery.of(m -> m.field(fieldName).lt(JsonData.of(value)))._toQuery();
    }

    public static <T> Query gt(String fieldName, T value) {
        return RangeQuery.of(m -> m.field(fieldName).gt(JsonData.of(value)))._toQuery();
    }

    public static <T> Query lte(String fieldName, T value) {
        return RangeQuery.of(m -> m.field(fieldName).lte(JsonData.of(value)))._toQuery();
    }

    public static <T> Query gte(String fieldName, T value) {
        return RangeQuery.of(m -> m.field(fieldName).gte(JsonData.of(value)))._toQuery();
    }

    public static Query like(String fieldName, String pattern) {
        return WildcardQuery.of(m -> m.field(fieldName).wildcard(pattern))._toQuery();
    }

    public static Query nestedLike(String path, String fieldName, String pattern) {
        return NestedQuery.of(n -> n.path(path).query(like(fieldName, pattern)))._toQuery();
    }

    public static Query nestedLike(String path, String fieldName, String pattern, Float boost) {
        return NestedQuery.of(n -> n.path(path).query(like(fieldName, pattern)).boost(boost))._toQuery();
    }

    public static Query contains(String fieldName, String value) {
        return TermQuery.of(m -> m.field(fieldName).value(value))._toQuery();
    }

    public static Query notIn(String fieldName, List<String> values) {
        Query anyMatch = TermsQuery.of(m -> m.field(fieldName).terms(TermsQueryField.of(t -> t.value(values.stream().map(FieldValue::of).collect(Collectors.toList())))))._toQuery();
        return BoolQuery.of(m -> m.mustNot(anyMatch))._toQuery();
    }

    public static Query inStringList(String fieldName, List<String> valueList) {
        return TermsQuery.of(m -> m.field(fieldName).terms(TermsQueryField.of(t -> t.value(valueList.stream().map(FieldValue::of).collect(Collectors.toList())))))._toQuery();
    }

    public static Query inIntegerList(String fieldName, List<Integer> valueList) {
        return TermsQuery.of(m -> m.field(fieldName).terms(TermsQueryField.of(t -> t.value(valueList.stream().map(FieldValue::of).collect(Collectors.toList())))))._toQuery();
    }

    public static Query inLongList(String fieldName, List<Long> valueList) {
        return TermsQuery.of(m -> m.field(fieldName).terms(TermsQueryField.of(t -> t.value(valueList.stream().map(FieldValue::of).collect(Collectors.toList())))))._toQuery();
    }

    public static Query isNull(String fieldName) {
        return BoolQuery.of(m -> m.mustNot(ExistsQuery.of(t -> t.field(fieldName))._toQuery()))._toQuery();
    }

    public static Query notNull(String fieldName) {
        return ExistsQuery.of(m -> m.field(fieldName))._toQuery();
    }

    public static Query isEmpty(String fieldName) {
        Query isNullOrNotExist = BoolQuery.of(m -> m.mustNot(ExistsQuery.of(t -> t.field(fieldName))._toQuery()))._toQuery();
        Query isEmpty = TermQuery.of(m -> m.field(fieldName).value(""))._toQuery();
        return BoolQuery.of(m -> m.should(isNullOrNotExist, isEmpty).minimumShouldMatch("1"))._toQuery();
    }

    public static <T> Query inRange(String fieldName, T min, T max) {
        RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field(fieldName);
        if (min != null) {
            if (min instanceof Date) {
                String minStr = DateFormatUtils.format((Date) min, "yyyy-MM-dd HH:mm:ss");
                rangeBuilder.gte(JsonData.of(minStr));
            } else {
                rangeBuilder.gte(JsonData.of(min));
            }
        }
        if (max != null) {
            if (max instanceof Date) {
                String maxStr = DateFormatUtils.format((Date) max, "yyyy-MM-dd HH:mm:ss");
                rangeBuilder.lte(JsonData.of(maxStr));
            } else {
                rangeBuilder.lte(JsonData.of(max));
            }
        }
        return rangeBuilder.build()._toQuery();
    }

    public static Query and(Query... queries) {
        return BoolQuery.of(m -> m.must(Arrays.stream(queries).collect(Collectors.toList())))._toQuery();
    }

    public static Query and(List<Query> queries) {
        return BoolQuery.of(m -> m.must(queries))._toQuery();
    }

    public static Query or(Query... queries) {
        return BoolQuery.of(m -> m.should(Arrays.stream(queries).collect(Collectors.toList())).minimumShouldMatch("1"))._toQuery();
    }

    public static Query or(List<Query> queries) {
        return BoolQuery.of(m -> m.should(queries).minimumShouldMatch("1"))._toQuery();
    }
}
