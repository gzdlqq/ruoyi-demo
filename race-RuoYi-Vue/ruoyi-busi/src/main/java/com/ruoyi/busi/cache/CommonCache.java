package com.ruoyi.busi.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.ruoyi.busi.domain.TRaceSchema;

//专门存储 赛制的本地缓存
public class CommonCache {

    //创建缓存，默认4毫秒过期
    public static TimedCache<Long, TRaceSchema> formatTimedCache = CacheUtil.newTimedCache(10000); //缓存过期时间 10s

    static {
        formatTimedCache.schedulePrune(10000); //10s 主动清理一次
    }

}
