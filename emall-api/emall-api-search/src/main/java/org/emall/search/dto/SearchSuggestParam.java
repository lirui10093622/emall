package org.emall.search.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Li Rui
 * @date 2025-09-28
 */
@Data
public class SearchSuggestParam implements Serializable {
    private String keyword;
    private Integer size;
}