package org.emall.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.emall.search.model.SearchSynonym;

import java.util.List;

@Mapper
public interface SearchSynonymMapper extends BaseMapper<SearchSynonym> {

    /**
     * 查询所有启用的同义词组
     * @return 同义词组列表
     */
    List<SearchSynonym> selectAllEnabled();

    /**
     * 根据关键词模糊查询同义词组
     * @param keyword 关键词
     * @return 包含关键词的同义词组
     */
    List<SearchSynonym> selectByKeywordLike(String keyword);
}