package org.emall.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.emall.search.model.SearchHistory;
import java.util.List;

@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

    /**
     * 根据用户ID查询搜索历史
     * @param userId 用户ID
     * @param size 查询数量
     * @return 搜索历史列表
     */
    List<SearchHistory> selectByUserId(@Param("userId") Long userId, @Param("size") Integer size);

    /**
     * 根据用户ID和关键词删除记录
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 删除行数
     */
    int deleteByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    /**
     * 批量插入搜索历史
     * @param list 搜索历史列表
     * @return 插入行数
     */
    int batchInsert(@Param("list") List<SearchHistory> list);
}