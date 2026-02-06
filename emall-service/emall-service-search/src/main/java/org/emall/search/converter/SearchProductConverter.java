package org.emall.search.converter;

import lombok.extern.slf4j.Slf4j;
import org.emall.search.context.SearchProductPageContext;
import org.emall.search.doc.ProductDoc;
import org.emall.search.vo.ProductVO;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author Li Rui
 * @date 2026-01-29
 */
@Slf4j
@Component
public class SearchProductConverter implements BiFunction<ProductDoc, SearchProductPageContext, ProductVO> {

    @Override
    public ProductVO apply(ProductDoc productDoc, SearchProductPageContext context) {
        return null;
    }
}