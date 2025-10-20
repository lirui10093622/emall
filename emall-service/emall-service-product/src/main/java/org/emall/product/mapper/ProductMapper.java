package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.emall.product.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}