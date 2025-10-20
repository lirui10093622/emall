package org.emall.search.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.emall.search.dto.SearchHistoryParam;
import org.emall.search.dto.SearchHistoryResult;
import org.emall.search.mapper.SearchHistoryMapper;
import org.emall.search.model.SearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索历史服务类（包含日志和参数检查）
 */
@Slf4j
@Service
public class SearchHistoryService {
    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    private static final String USER_HISTORY_KEY = "search:history:user:";

    /**
     * 查询用户搜索历史（包含日志和参数检查）
     */
    public SearchHistoryResult getUserSearchHistory(SearchHistoryParam param) {
        // 1. 参数校验
        if (param == null || param.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (param.getSize() == null || param.getSize() < 1 || param.getSize() > 100) {
            param.setSize(20);
        }

        // 2. 查询用户历史记录
        List<SearchHistory> histories = searchHistoryMapper.selectByUserId(
                param.getUserId(),
                param.getSize()
        );

        // 3. 转换结果
        SearchHistoryResult result = new SearchHistoryResult();
        result.setUserId(param.getUserId());
        result.setHistoryList(histories.stream().map(SearchHistory::getKeyword).collect(Collectors.toList()));
        return result;
    }

    /**
     * 删除搜索历史（包含日志和参数检查）
     */
    public boolean deleteSearchHistory(SearchHistoryParam param) {
        if (param == null || param.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        QueryWrapper<SearchHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", param.getUserId());
        // 单个删除
        if (param.getId() != null) {
            queryWrapper.eq("id", param.getId());
        }
        // 批量删除（按关键词）
        if (!CollectionUtils.isEmpty(param.getKeywords())) {
            queryWrapper.in("keyword", param.getKeywords());
        }
        return searchHistoryMapper.delete(queryWrapper) > 0;
    }
}