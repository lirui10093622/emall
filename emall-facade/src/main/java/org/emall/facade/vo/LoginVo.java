package org.emall.facade.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    private String username;
    private String phone;
    private String email;
    private String password;
    private String captchaText;
}