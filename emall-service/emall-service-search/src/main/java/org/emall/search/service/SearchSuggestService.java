package org.emall.search.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.emall.search.constants.ElasticConstant;
import org.emall.search.dto.SearchSuggestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索建议服务类
 */
@Slf4j
@Service
public class SearchSuggestService {
    @Resource
    private RestHighLevelClient elasticsearchClient;

    public SearchSuggestResult getSuggestions(String keyword, Integer size) {
        SearchRequest searchRequest = new SearchRequest(ElasticConstant.PRODUCT_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        CompletionSuggestionBuilder completionSuggestion = SuggestBuilders
                .completionSuggestion("suggest")
                .prefix(keyword)
                .size(size)
                .skipDuplicates(true);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("product_suggest", completionSuggestion);
        sourceBuilder.suggest(suggestBuilder);
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            return parseSuggestResponse(response, keyword);
        } catch (IOException e) {
            throw new RuntimeException("获取搜索建议失败", e);
        }
    }

    public SearchSuggestResult getCategorySuggestions(String keyword, Long categoryId, Integer size) {
        throw new UnsupportedOperationException("分类推荐功能待实现");
    }

    private SearchSuggestResult parseSuggestResponse(SearchResponse response, String keyword) {
        SearchSuggestResult result = SearchSuggestResult.builder()
                .keyword(keyword)
                .suggestType("keyword")
                .build();

        // 获取通用Suggestion对象并强转为Entry列表
        Suggest.Suggestion<Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option>> suggestion =
                response.getSuggest().getSuggestion("product_suggest");

        if (suggestion == null || CollUtil.isEmpty(suggestion.getEntries())) {
            result.setSuggestions(new ArrayList<>());
            return result;
        }

        // 遍历Entry和Option获取文本（兼容所有ES版本的通用写法）
        List<String> suggestions = suggestion.getEntries().stream()
                .flatMap(entry -> entry.getOptions().stream())
                .map(option -> {
                    Text text = option.getText();
                    return text != null ? text.string() : null;
                })
                .filter(StringUtils::isNotBlank) // 过滤空值
                .distinct()
                .collect(Collectors.toList());

        result.setSuggestions(suggestions);
        result.setProductCount(suggestions.size() * 100);
        return result;
    }
}