package org.emall.product.api;

import org.emall.common.api.EmallService;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.product.dto.ProductDto;

import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
public interface EmallProductService extends EmallService {

    EmallResponse<List<ProductDto>> getAllProducts(EmallRequest<Void> request);
}