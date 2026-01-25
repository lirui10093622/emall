package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.product.model.ProductInventory;

@Mapper
public interface ProductInventoryMapper extends BaseMapper<ProductInventory> {
}