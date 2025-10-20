package org.emall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.emall.product.model.Product;
import org.emall.product.model.ProductAttribute;
import org.emall.product.model.ProductCategory;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Data
@AllArgsConstructor
public class ProductDto implements Serializable {
    private Product product;
    private ProductCategory category;
    private List<ProductAttribute> attributes;
}