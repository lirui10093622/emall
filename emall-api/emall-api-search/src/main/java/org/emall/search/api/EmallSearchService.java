package org.emall.search.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.emall.common.api.EmallService;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.dto.ProductSearchPageParam;
import org.emall.search.dto.ProductSearchPageVO;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallSearchService extends EmallService {

    EmallResponse<IPage<ProductSearchPageVO>> searchProductPage(EmallRequest<ProductSearchPageParam> request);
}