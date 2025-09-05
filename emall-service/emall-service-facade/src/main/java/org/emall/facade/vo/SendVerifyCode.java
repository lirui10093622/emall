package org.emall.facade.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@Data
public class SendVerifyCode implements Serializable {
    private String scene;
}