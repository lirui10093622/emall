package org.emall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.emall.common.model.user.User;

import java.util.Optional;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM t_user WHERE name = #{name}")
    Optional<User> selectByName(@Param("name") String name);

    @Select("SELECT * FROM t_user WHERE phone = #{phone}")
    Optional<User> selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM t_user WHERE email = #{email}")
    Optional<User> selectByEmail(@Param("email") String email);

    Optional<User> selectByAccount(@Param("account") String account);
}