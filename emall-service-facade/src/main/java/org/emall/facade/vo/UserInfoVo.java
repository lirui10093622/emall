package org.emall.facade.vo;

import lombok.Data;
import org.emall.user.api.dto.UserInfoDto;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String email;

    public static UserInfoVo from(UserInfoDto userInfoDto) {
        UserInfoVo vo = new UserInfoVo();
        vo.setId(userInfoDto.getId());
        vo.setName(userInfoDto.getName());
        vo.setPassword(userInfoDto.getPassword());
        vo.setPhone(userInfoDto.getPhone());
        vo.setEmail(userInfoDto.getEmail());
        return vo;
    }
}