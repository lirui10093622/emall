package org.emall.facade.vo;

import org.emall.user.api.dto.UserInfoDto;

public class UserInfoVo {

    public static UserInfoVo from(UserInfoDto userInfoDto) {
        UserInfoVo vo = new UserInfoVo();
        return vo;
    }
}