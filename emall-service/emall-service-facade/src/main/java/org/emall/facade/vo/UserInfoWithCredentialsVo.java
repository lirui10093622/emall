package org.emall.facade.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-08-11
 */
@AllArgsConstructor
@Data
public class UserInfoWithCredentialsVo implements Serializable {
    private String type;
    private String credentials;
    private UserInfoVo userInfo;
}