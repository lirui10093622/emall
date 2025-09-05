package org.emall.sms.api.dto;

import lombok.Data;
import org.emall.sms.api.enums.SmsSceneEnum;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@Data
public class SmsDto implements Serializable {
    private String phone;
    private SmsSceneEnum scene;
    private Long userId;
    private long ttl;
}