package org.emall.user.api.request;

import lombok.Data;
import org.emall.common.request.EmallRequest;
import org.emall.user.api.enums.LoginType;

@Data
public class LoginRequest extends EmallRequest {
    private LoginType loginType;
    private String account;
    private transient String password;
    private Integer sourceType;
    private String loginIp;
}