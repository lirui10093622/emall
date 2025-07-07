package org.emall.user.api.request;

import lombok.Data;
import org.emall.common.request.EmallRequest;

@Data
public class RegisterRequest extends EmallRequest {
    private String username;
    private String password;
    private String phone;
    private String email;
}