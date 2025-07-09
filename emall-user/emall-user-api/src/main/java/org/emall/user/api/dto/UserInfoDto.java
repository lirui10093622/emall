package org.emall.user.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoDto implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;
}