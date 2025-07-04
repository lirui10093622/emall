package org.emall.user.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.emall.user.model.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> {
    @Select("select * from t_order_task where vendor_id = #{vendorId} and vendor_order_id = #{vendorOrderId}")
    User selectByVendorIdAndVendorOrderId(@Param("vendorId") Long vendorId, @Param("vendorOrderId") String vendorOrderId);
}