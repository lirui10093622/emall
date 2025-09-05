package org.emall.user.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@Data
public class ChangeMyPasswordDto implements Serializable {
    private String oldPassword;
    private String newPassword;
    private String verifyCode;
}