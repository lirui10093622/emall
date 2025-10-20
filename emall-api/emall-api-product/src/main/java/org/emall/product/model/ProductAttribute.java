package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品属性表实体类（对应表：t_product_attribute）
 */
@Data
@TableName("t_product_attribute")
public class ProductAttribute implements Serializable {
    /**
     * 属性ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID（外键：关联t_product.id）
     */
    private Long productId;

    /**
     * 属性名称（如"重量"、"材质"）
     */
    private String attributeName;

    /**
     * 属性值（如"500g"、"纯棉"）
     */
    private String attributeValue;

    /**
     * 属性类型（0-通用属性/1-规格属性）
     */
    private Integer type;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（false-正常/true-删除）
     */
    @TableLogic(value = "false", delval = "true")
    private Boolean deleted;
}