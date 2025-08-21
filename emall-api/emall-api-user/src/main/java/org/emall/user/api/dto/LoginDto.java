package org.emall.user.api.dto;

import lombok.Data;
import org.emall.common.request.EmallRequest;
import org.emall.user.api.enums.AccountType;
import org.emall.user.api.enums.DeviceType;

import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    private AccountType accountType;
    private String account;
    private String password;
    private DeviceType device;
    private String ip;
}