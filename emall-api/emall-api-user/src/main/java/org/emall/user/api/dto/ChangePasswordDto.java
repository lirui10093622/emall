package org.emall.user.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@NoArgsConstructor
@Data
public class ChangePasswordDto implements Serializable {
    private Long userId;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordDto(LoginUser loginUser, ChangeMyPasswordDto changeMyPasswordDto) {
        this.userId = loginUser.getId();
        this.oldPassword = changeMyPasswordDto.getOldPassword();
        this.newPassword = changeMyPasswordDto.getNewPassword();
    }
}