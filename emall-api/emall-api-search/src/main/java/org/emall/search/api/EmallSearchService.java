package org.emall.search.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.emall.common.api.EmallService;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.search.doc.ProductDoc;
import org.emall.search.dto.ProductSearchPageParam;
import org.emall.search.dto.ProductSearchPageVO;

import java.util.List;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
public interface EmallSearchService extends EmallService {

    EmallResponse<IPage<ProductSearchPageVO>> searchProductPage(EmallRequest<ProductSearchPageParam> request);

    EmallResponse<Boolean> sync(EmallRequest<List<ProductDoc>> request);

    EmallResponse<List<ProductDoc>> search(EmallRequest<String> request);
}