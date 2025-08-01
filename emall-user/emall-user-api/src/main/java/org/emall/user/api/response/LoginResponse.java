package org.emall.user.api.response;

import lombok.Data;
import org.emall.common.response.EmallResponse;
import org.emall.user.api.dto.UserInfoDto;

@Data
public class LoginResponse extends EmallResponse {
    private UserInfoDto userInfo;
}