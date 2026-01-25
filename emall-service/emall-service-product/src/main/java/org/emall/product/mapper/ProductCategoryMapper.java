package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.product.model.ProductCategory;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}