package org.emall.user.api.dto;

import lombok.Data;
import org.emall.user.model.User;

import java.io.Serializable;

@Data
public class UserInfoDto implements Serializable {
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String email;

    public static UserInfoDto from(User user) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(user.getId());
        userInfoDto.setName(user.getName());
        userInfoDto.setPhone(user.getPhone());
        userInfoDto.setEmail(user.getEmail());
        return userInfoDto;
    }
}