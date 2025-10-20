package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.emall.product.model.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}