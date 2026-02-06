package org.emall.search.service;

import lombok.extern.slf4j.Slf4j;
import org.emall.search.doc.ProductDoc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class SearchCoreServiceTest {
    @Autowired
    private ProductElasticSearchQueryService searchCoreService;

    @Test
    void sync() {
    }

    @Test
    void search() {
        List<ProductDoc> productDocList = searchCoreService.search("手机");
        log.info("productDocList: {}", productDocList);
    }

    @Test
    void searchProduct() {
    }

    @Test
    void getHotWords() {
    }
}