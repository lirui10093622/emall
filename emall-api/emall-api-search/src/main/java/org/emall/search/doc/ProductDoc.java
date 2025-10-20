package org.emall.search.doc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Data
public class ProductDoc implements Serializable {
    private Long id;
    private String name;
    private String code;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private List<ProductAttributeField> attributes;
}