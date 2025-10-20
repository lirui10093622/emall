package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品图片表实体类（对应表：t_product_image）
 */
@Data
@TableName("t_product_image")
public class ProductImage implements Serializable {
    /**
     * 图片ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID（外键：关联t_product.id）
     */
    private Long productId;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 排序值（0为主图，升序排列）
     */
    private Integer sort;

    /**
     * 图片类型（1-商品图/2-详情图）
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