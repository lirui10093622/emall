package org.emall.search.doc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Data
public class ProductAttributeField implements Serializable {
    private String name;
    private String value;
}