package org.emall.user.api.dto;

import lombok.Data;
import org.emall.user.model.Permission;
import org.emall.user.model.Role;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUser implements Serializable {

    private boolean authenticated;

    private Long id;
    private String name;
    private LoginInfo loginInfo;

    private List<Role> roles;
    private List<Permission> permissions;

    // 授权上下文信息
    // private boolean authorized;
    // private HttpServletRequest request;
    // private HttpServletResponse response;
    // private Object handler;
}