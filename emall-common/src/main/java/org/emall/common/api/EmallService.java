package org.emall.common.api;

import org.emall.common.api.dto.HealthDto;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
public interface EmallService {

    EmallResponse<HealthDto> healthCheck(EmallRequest<Void> request) throws EmallException;
}