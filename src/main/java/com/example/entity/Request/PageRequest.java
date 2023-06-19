package com.example.entity.Request;

import lombok.Data;

@Data
public class PageRequest {
    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 当前页最大数据
     */
    private Integer pageSize;
}
