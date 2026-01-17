package com.easychat.redis;

import io.lettuce.core.RedisConnectionException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration  // Marks this class as a Spring configuration class.
public class RedisConfig<V> {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host:}")
    private String redisHost;

    @Value("${spring.redis.port:}")
    private Integer redisPort;

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        try {
            // Create a Redisson configuration object.
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);

            // Create and return the Redisson client instance.
            RedissonClient redissonClient = Redisson.create(config);
            return redissonClient;
        } catch (RedisConnectionException e) {
            logger.error("Redis configuration error. Please verify Redis connection settings.");
        }
        return null;
    }


    @Bean("redisTemplate")
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);

        // Configure key serialization as String.
        template.setKeySerializer(RedisSerializer.string());

        // Configure value serialization as JSON.
        template.setValueSerializer(RedisSerializer.json());

        // Configure hash key serialization as String.
        template.setHashKeySerializer(RedisSerializer.string());

        // Configure hash value serialization as JSON.
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }
}