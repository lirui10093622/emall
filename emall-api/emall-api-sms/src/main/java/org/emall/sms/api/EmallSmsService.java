package org.emall.sms.api;

import org.emall.common.api.EmallService;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.sms.api.dto.SmsDto;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallSmsService extends EmallService {

    EmallResponse<Void> send(EmallRequest<SmsDto> request) throws EmallException;

    EmallResponse<String> get(EmallRequest<SmsDto> request) throws EmallException;
}