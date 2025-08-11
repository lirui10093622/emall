package org.emall.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Li Rui
 * @date 2025-08-07
 */
@Data
public class LoginInfo implements Serializable {
    private Date time;
    private String ip;
}