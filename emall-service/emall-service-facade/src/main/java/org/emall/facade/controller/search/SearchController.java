package org.emall.facade.controller.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.api.EmallSearchService;
import org.emall.search.doc.ProductDoc;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Slf4j
@RestController
@RequestMapping("api/search")
public class SearchController {
    @DubboReference
    private EmallSearchService emallSearchService;

    @GetMapping("searchProduct")
    public EmallResponse<List<ProductDoc>> searchProduct(@Param("kw") String kw) {
        return emallSearchService.search(new EmallRequest<>(kw));
    }
}