package com.owlexpress.hub.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+redisHost+":"+redisPort);
        return Redisson.create(config);
    }


    @Bean
    public CacheManager cacheManager(
            RedissonClient redissonClient
    ) {
        Map<String, org.redisson.spring.cache.CacheConfig> config = new HashMap<>();

        config.put("shortestRoutes", new CacheConfig(
                TimeUnit.DAYS.toMillis(7),   // TTL: 7 days
                TimeUnit.DAYS.toMillis(1)    // Max idle time: 1 day
        ));

        return new RedissonSpringCacheManager(redissonClient, config);
    }
}