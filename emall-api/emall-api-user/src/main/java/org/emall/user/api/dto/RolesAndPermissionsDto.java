package org.emall.user.api.dto;

import lombok.Data;
import org.emall.user.model.Permission;
import org.emall.user.model.Role;

import java.io.Serializable;
import java.util.List;

@Data
public class RolesAndPermissionsDto implements Serializable {
    private List<Role> roles;
    private List<Permission> permissions;
}