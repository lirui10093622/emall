package org.emall.facade.vo;

import lombok.Data;
import org.emall.common.api.dto.HealthDto;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Data
public class HealthVO implements Serializable {
    private String appName;
    private String status;

    public static HealthVO from(HealthDto healthDto) {
        HealthVO healthVO = new HealthVO();
        healthVO.setAppName(healthDto.getAppName());
        healthVO.setStatus(healthDto.getStatus());
        return healthVO;
    }
}