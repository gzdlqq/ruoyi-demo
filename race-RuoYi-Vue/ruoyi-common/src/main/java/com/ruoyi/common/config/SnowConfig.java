package com.ruoyi.common.config;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowConfig {

    @Bean
    public SnowflakeGenerator snowflakeGenerator(){
        return new SnowflakeGenerator();
    }
}
