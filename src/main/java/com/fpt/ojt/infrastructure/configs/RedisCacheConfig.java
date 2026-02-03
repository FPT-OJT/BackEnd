package com.fpt.ojt.infrastructure.configs;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableCaching
@Configuration
public class RedisCacheConfig {
        private final int DEFAULT_CACHE_TTL = 10;
        public ObjectMapper redisObjectMapper() {
                return JsonMapper.builder()
                                .addModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .build();
        }

        @Bean
        public RedisCacheConfiguration redisCacheConfiguration() {
                Jackson2JsonRedisSerializer<Object> serializer =
                                new Jackson2JsonRedisSerializer<>(Object.class);

                serializer.setObjectMapper(redisObjectMapper());
                return RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(DEFAULT_CACHE_TTL))
                                .disableCachingNullValues()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(serializer));
        }

        @Bean
        public RedisCacheManager cacheManager(
                        RedisConnectionFactory redisConnectionFactory,
                        RedisCacheConfiguration redisCacheConfiguration) {
                return RedisCacheManager.builder(redisConnectionFactory)
                                .cacheDefaults(redisCacheConfiguration)
                                .build();
        }
}