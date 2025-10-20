package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表实体类（对应表：t_product）
 */
@Data
@TableName("t_product")
public class Product implements Serializable {
    /**
     * 商品ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品编码（业务唯一）
     */
    private String code;

    /**
     * 分类ID（外键：关联t_product_category.id）
     */
    private Long categoryId;

    /**
     * 品牌ID（外键：关联brand.id，可为空）
     */
    private Long brandId;

    /**
     * 商品售价（精确到分）
     */
    private BigDecimal price;

    /**
     * 市场价（可为空）
     */
    private BigDecimal marketPrice;

    /**
     * 商品状态（枚举：0-下架/1-上架/2-售罄）
     */
    private Integer status;

    /**
     * 商品描述（HTML格式）
     */
    private String description;

    /**
     * 主图URL
     */
    private String mainImage;

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