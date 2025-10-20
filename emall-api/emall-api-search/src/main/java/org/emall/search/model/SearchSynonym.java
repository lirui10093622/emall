package org.emall.search.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 搜索同义词实体类
 * 对应数据库表：t_search_synonym
 */
@Data
@TableName("t_search_synonym")
public class SearchSynonym {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 同义词组（逗号分隔，如"手机,智能机,移动电话"）
     */
    private String wordGroup;

    /**
     * 状态（1-启用，0-禁用）
     */
    private Integer status = 1;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人（系统默认：SYSTEM）
     */
    private String createUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人（系统默认：SYSTEM）
     */
    private String updateUser;
}