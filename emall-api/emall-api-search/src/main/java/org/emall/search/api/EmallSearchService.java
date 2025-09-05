package org.emall.search.api;

import org.emall.api.EmallService;
import org.emall.common.exception.EmallException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallSearchService extends EmallService {

    EmallResponse<Void> search(EmallRequest<Void> request) throws EmallException;
}