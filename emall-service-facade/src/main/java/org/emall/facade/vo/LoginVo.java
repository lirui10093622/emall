package org.emall.facade.vo;

import lombok.Data;
import org.emall.user.api.enums.AccountType;
import org.emall.user.api.enums.DeviceType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    private AccountType accountType;
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String captchaText;
    private DeviceType device;
}