package com.ruoyi.common.core.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResp {
    private List<Object> list;
    private int totalCount;
    private int pageNum;
    private int pageSize;


//    public PageResp builder(){
//
//    }
}
