package com.owlexpress.delivery.infrastructure.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@ComponentScan
public class RedissonConfig {

    public final static Integer DELIVERY_TTL_HOURS = 48;
    public final static Integer DELIVERY_IDLE_HOURS = 24;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useSingleServer().setAddress("redis://localhost:6378");
        config.setCodec(new JsonJacksonCodec(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())));

        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(
            RedissonClient redissonClient

    ) {
        // 캐시 설정
        Map<String, CacheConfig> config = new HashMap<>();
        config.put("delivery", new CacheConfig(
                TimeUnit.HOURS.toMillis(DELIVERY_TTL_HOURS), // TTL
                TimeUnit.HOURS.toMillis(DELIVERY_IDLE_HOURS) // Max idle time
        ));

        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient, config);

        redissonSpringCacheManager.setCodec(new JsonJacksonCodec(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())));

        return redissonSpringCacheManager;
    }
}
