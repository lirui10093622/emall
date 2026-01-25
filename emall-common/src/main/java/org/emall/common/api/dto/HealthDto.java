package org.emall.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@Data
@AllArgsConstructor
public class HealthDto implements Serializable {
    private String appName;
    private String status;
}