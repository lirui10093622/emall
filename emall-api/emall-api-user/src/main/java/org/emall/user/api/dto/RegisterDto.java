package org.emall.user.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {
    private String name;
    private String password;
    private String phone;
    private String email;
}