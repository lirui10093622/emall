package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU表实体类（对应表：t_product_sku）
 */
@Data
@TableName("t_product_sku")
public class ProductSku implements Serializable {
    /**
     * SKU ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID（外键：关联t_product.id）
     */
    private Long productId;

    /**
     * SKU编码（唯一，如PROD001-RED-L）
     */
    private String skuCode;

    /**
     * 规格JSON（如{"颜色":"红色","尺寸":"L"}）
     */
    private String specJson;

    /**
     * SKU售价（精确到分）
     */
    private BigDecimal price;

    /**
     * 库存数量（冗余字段）
     */
    private Integer stock;

    /**
     * SKU图片（为空时使用商品主图）
     */
    private String image;

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