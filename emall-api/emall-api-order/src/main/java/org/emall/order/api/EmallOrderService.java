package org.emall.order.api;

import org.emall.common.api.EmallService;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallOrderService extends EmallService {

    EmallResponse<Void> createOrder(EmallRequest<Void> request) throws EmallException;
}