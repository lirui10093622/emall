package org.emall.facade.controller.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResult;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.facade.annotation.Auth;
import org.emall.product.api.EmallProductService;
import org.emall.product.dto.ProductDto;
import org.emall.search.api.EmallSearchService;
import org.emall.search.doc.ProductAttributeField;
import org.emall.search.doc.ProductDoc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Slf4j
@Auth(needLogin = false)
@RestController
@RequestMapping("api/product")
public class ProductController {
    @DubboReference
    private EmallProductService emallProductService;
    @DubboReference
    private EmallSearchService emallSearchService;

    @PostMapping("sync")
    public EmallResponse<Boolean> sync() {
        EmallResponse<List<ProductDto>> response = emallProductService.getAllProducts(new EmallRequest<>());
        if (response.isSuccess()) {
            List<ProductDoc> productDocs = response.getData().stream().map(dto -> {
                ProductDoc productDoc = new ProductDoc();
                productDoc.setId(dto.getProduct().getId());
                productDoc.setName(dto.getProduct().getName());
                productDoc.setCode(dto.getProduct().getCode());
                productDoc.setCategoryId(dto.getProduct().getCategoryId());
                productDoc.setCategoryName(dto.getCategory().getName());
                productDoc.setBrandId(dto.getProduct().getBrandId());
                productDoc.setAttributes(dto.getAttributes().stream().map(attribute -> {
                    ProductAttributeField field = new ProductAttributeField();
                    field.setName(attribute.getAttributeName());
                    field.setValue(attribute.getAttributeValue());
                    return field;
                }).collect(Collectors.toList()));
                return productDoc;
            }).collect(Collectors.toList());
            return emallSearchService.sync(new EmallRequest<>(productDocs));
        }
        return EmallResponse.fail(ApiResult.FAIL);
    }
}