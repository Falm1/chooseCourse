package com.example.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedissonConfig {

    private String host;                        //连接主机号，直接从yaml中获取

    private String port;                        //连接端口号

    private String password;

    @Bean
    public RedissonClient getRedissonClient(){
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(3).setPassword(password);

        return Redisson.create(config);
    }
}
