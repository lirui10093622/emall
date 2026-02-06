package org.emall.search.builder.product;

import co.elastic.clients.elasticsearch._types.ScriptSortType;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.emall.search.param.SearchProductPageParam;
import org.emall.user.api.dto.LoginUser;

import java.util.List;

import static org.emall.search.builder.product.ProductScriptBuilder.curStageOperatorOrder;

/**
 * @author Li Rui
 * @date 2026-01-29
 */
public class ProductSortOptionsBuilder {

    public static List<SortOptions> sorters(SearchProductPageParam param) {
        List<SortOptions> sortList = Lists.newArrayList();
        if (hasKeyword(param.getKeywords())) {
            sortList.add(SortOptions.of(a -> a.field(b -> b.field("_score").order(SortOrder.Desc))));
        } else {
            sortList.add(sortByIsCurStageOperator(param.getLoginUser().getId()));
        }
        sortList.add(SortOptions.of(a -> a.field(b -> b.field("id").order(SortOrder.Desc))));
        sortList.add(SortOptions.of(a -> a.field(b -> b.field("_doc").order(SortOrder.Asc))));
        return sortList;
    }

    private static boolean hasKeyword(List<String> keywordList) {
        return CollectionUtils.isNotEmpty(keywordList) && keywordList.stream().anyMatch(StringUtils::isNotBlank);
    }

    private static SortOptions sortByIsCurStageOperator(LoginUser loginUser) {
        return SortOptions.of(so -> so.script(sc -> sc.script(curStageOperatorOrder(loginUser.getId())).type(ScriptSortType.Number)
                .order(SortOrder.Asc)));
    }
}
