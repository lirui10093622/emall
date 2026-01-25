package org.emall.product.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.api.dto.HealthDto;
import org.emall.common.enums.AppStatusEnum;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.product.dto.ProductDto;
import org.emall.product.mapper.ProductAttributeMapper;
import org.emall.product.mapper.ProductCategoryMapper;
import org.emall.product.mapper.ProductMapper;
import org.emall.product.model.Product;
import org.emall.product.model.ProductAttribute;
import org.emall.product.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Slf4j
@DubboService
public class EmallProductServiceImpl implements EmallProductService {
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException {
        return EmallResponse.success(new HealthDto(appName, AppStatusEnum.RUNNING.name()));
    }

    @Override
    public EmallResponse<List<ProductDto>> getAllProducts(EmallRequest<Void> request) {
        List<Product> productList = productMapper.selectList(null);
        List<Long> productIds = productList.stream().map(Product::getId).collect(Collectors.toList());
        List<Long> categoryIds = productList.stream().map(Product::getCategoryId).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(Wrappers.lambdaQuery(ProductCategory.class).in(ProductCategory::getId, categoryIds));
        Map<Long, ProductCategory> productCategoryMap = productCategoryList.stream().collect(Collectors.toMap(ProductCategory::getId, productCategory -> productCategory));
        List<ProductAttribute> productAttributeList = productAttributeMapper.selectList(Wrappers.lambdaQuery(ProductAttribute.class).in(ProductAttribute::getProductId, productIds));
        Map<Long, List<ProductAttribute>> productAttributeMap = productAttributeList.stream().collect(Collectors.groupingBy(ProductAttribute::getProductId));
        return EmallResponse.success(productList.stream().map(product -> new ProductDto(product, productCategoryMap.get(product.getCategoryId()), productAttributeMap.get(product.getId()))).collect(Collectors.toList()));
    }
}
