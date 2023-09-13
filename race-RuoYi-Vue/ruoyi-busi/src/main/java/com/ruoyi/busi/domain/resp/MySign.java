package com.ruoyi.busi.domain.resp;

import com.ruoyi.busi.domain.TSign;
import lombok.Data;

@Data
public class MySign extends TSign {


    //赛事名称
    private String name;

    //赛事介绍
    private String description;

    // banner_url
    private String bannerUrl;

}
