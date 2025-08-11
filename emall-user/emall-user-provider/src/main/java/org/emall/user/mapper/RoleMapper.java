package org.emall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.common.model.user.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
