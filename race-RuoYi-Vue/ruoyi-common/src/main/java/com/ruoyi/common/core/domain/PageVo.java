package com.ruoyi.common.core.domain;

import lombok.Data;

/**
 * 专门接受请求参数
 */
@Data
public class PageVo {

    private int pageNum = 1;
    private int pageSize = 10;


}
