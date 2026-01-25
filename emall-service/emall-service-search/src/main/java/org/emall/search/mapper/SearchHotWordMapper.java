package org.emall.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.emall.search.model.SearchHotWord;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SearchHotWordMapper extends BaseMapper<SearchHotWord> {

    /**
     * 根据分类和周期查询热门词
     * @param categoryId 分类ID
     * @param period 统计周期（day/week/month）
     * @param date 统计日期
     * @param size 查询数量
     * @return 热门词列表
     */
    List<SearchHotWord> selectHotWords(
            @Param("categoryId") Long categoryId,
            @Param("period") String period,
            @Param("date") LocalDate date,
            @Param("size") Integer size
    );

    /**
     * 更新关键词搜索次数
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param period 统计周期
     * @param date 统计日期
     * @return 更新行数
     */
    int incrementSearchCount(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("period") String period,
            @Param("date") LocalDate date
    );

    /**
     * 删除指定日期前的历史数据
     * @param date 截止日期
     * @return 删除行数
     */
    int deleteBeforeDate(@Param("date") LocalDate date);
}