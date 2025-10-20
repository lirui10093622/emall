package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类表实体类（对应表：t_product_category）
 */
@Data
@TableName("t_product_category")
public class ProductCategory implements Serializable {
    /**
     * 分类ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID（顶级分类为0）
     */
    private Long parentId;

    /**
     * 分类级别（1-一级/2-二级/3-三级）
     */
    private Integer level;

    /**
     * 排序值（升序）
     */
    private Integer sort;

    /**
     * 状态（0-禁用/1-启用）
     */
    private Integer status;

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