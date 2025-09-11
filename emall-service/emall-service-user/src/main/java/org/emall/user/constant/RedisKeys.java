package org.emall.user.constant;

import lombok.Data;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
@Data
public class RedisKeys {
    public static final String REDIS_KEY_USER_ROLES_AND_PERMISSIONS = "user_roles_and_permissions:%s";
}