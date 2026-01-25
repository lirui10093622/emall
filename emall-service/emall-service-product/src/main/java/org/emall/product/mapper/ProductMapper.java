package org.emall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.product.model.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}