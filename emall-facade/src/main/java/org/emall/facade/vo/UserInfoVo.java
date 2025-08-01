package org.emall.facade.vo;

import lombok.Data;
import org.emall.user.api.dto.UserInfoDto;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;

    public static UserInfoVo from(UserInfoDto userInfoDto) {
        UserInfoVo vo = new UserInfoVo();
        vo.setUserId(userInfoDto.getUserId());
        vo.setUsername(userInfoDto.getUsername());
        vo.setPassword(userInfoDto.getPassword());
        vo.setPhone(userInfoDto.getPhone());
        vo.setEmail(userInfoDto.getEmail());
        return vo;
    }
}