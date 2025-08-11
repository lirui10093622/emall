package org.emall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.common.model.user.Permission;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
