package org.emall.search.converter;

import lombok.extern.slf4j.Slf4j;
import org.emall.search.context.ElasticSearchContext;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.ProductSearchPageVO;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author Li Rui
 * @date 2026-01-29
 */
@Slf4j
@Component
public class SearchProductConverter implements BiFunction<ProductDoc, ElasticSearchContext, ProductSearchPageVO> {

    @Override
    public ProductSearchPageVO apply(ProductDoc productDoc, ElasticSearchContext context) {
        return null;
    }
}