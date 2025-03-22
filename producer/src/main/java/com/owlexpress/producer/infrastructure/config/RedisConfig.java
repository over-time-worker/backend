package com.owlexpress.producer.infrastructure.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json();
    }

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory
    ) {
        //설정 구성을 먼저 진행
        //Redis를 이용해서 Spring Cache를 사용할 때
        // Redis 관련 설정을 모아두는 클래
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                                                                 .disableCachingNullValues()
                                                                                 .entryTtl(Duration.ofSeconds(
                                                                                         60*60)) // TTL(Time To Live)
                                                                                 .computePrefixWith(
                                                                                         CacheKeyPrefix.simple()) // 캐시를 구분하는 접두사
                                                                                 .serializeValuesWith(
                                                                                         fromSerializer(
                                                                                                 RedisSerializer.java()));

        return RedisCacheManager.builder(redisConnectionFactory)
                                .cacheDefaults(redisCacheConfiguration)
                                .build();
    }
}