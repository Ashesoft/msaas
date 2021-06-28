package com.longrise.msaas.global.domain;

import java.io.Serializable;

/**
 * 分页
 */
public class PaginationHelper implements Serializable {
    private Integer pageNum = -1;
    private Integer pageSize = -1;
    private Integer pageCount = -1;
    private String sql;
    private boolean isFirstPage = false;
    private boolean isLastPage = false;
}
