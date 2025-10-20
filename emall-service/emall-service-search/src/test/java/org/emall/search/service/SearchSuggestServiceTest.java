package org.emall.search.service;

import lombok.extern.slf4j.Slf4j;
import org.emall.search.dto.SearchSuggestResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SearchSuggestServiceTest {
    @Autowired
    private SearchSuggestService searchSuggestService;

    @Test
    void getSuggestions() {
        SearchSuggestResult searchSuggestResult = searchSuggestService.getSuggestions("手机", 10);
        log.info("searchSuggestResult: {}", searchSuggestResult);
    }
}