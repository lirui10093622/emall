package org.emall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.emall.common.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMapper extends BaseMapper<User> {

    Optional<User> selectByUsernameOrPhoneOrEmail(@Param("account") String account);

    @Select("SELECT * FROM t_user WHERE username = #{username}")
    Optional<User> selectByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_user WHERE phone = #{phone}")
    Optional<User> selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM t_user WHERE email = #{email}")
    Optional<User> selectByEmail(@Param("email") String email);
}