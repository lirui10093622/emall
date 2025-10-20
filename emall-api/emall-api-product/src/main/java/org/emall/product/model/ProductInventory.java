package org.emall.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存表实体类（对应表：t_product_inventory）
 */
@Data
@TableName("t_product_inventory")
public class ProductInventory implements Serializable {
    /**
     * 库存ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * SKU ID（外键：关联t_product_sku.id）
     */
    private Long skuId;

    /**
     * 总库存
     */
    private Integer totalStock;

    /**
     * 锁定库存（下单未支付占用）
     */
    private Integer lockedStock;

    /**
     * 可用库存（=总库存-锁定库存）
     */
    private Integer availableStock;

    /**
     * 乐观锁版本（防并发）
     */
    @Version
    private Integer version;

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