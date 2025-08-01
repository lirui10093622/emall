package org.emall.user.api.dto;

import lombok.Data;
import org.emall.common.model.user.Permission;
import org.emall.common.model.user.Role;

import java.io.Serializable;
import java.util.List;

@Data
public class RolesAndPermissionsDto implements Serializable {
    private List<Role> roles;
    private List<Permission> permissions;
}