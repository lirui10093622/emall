package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.emall.product.model.ProductInventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductInventoryMapper extends BaseMapper<ProductInventory> {
}