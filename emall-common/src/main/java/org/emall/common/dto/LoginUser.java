package org.emall.common.dto;

import lombok.Data;
import org.emall.common.model.user.Permission;
import org.emall.common.model.user.Role;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUser implements Serializable {
    // 是否匿名
    private boolean anonymous;

    // 基本信息
    private Long userId;
    private String loginName;
    private String nickname;

    // 角色与权限
    private List<Role> roles;
    private List<Permission> permissions;
}