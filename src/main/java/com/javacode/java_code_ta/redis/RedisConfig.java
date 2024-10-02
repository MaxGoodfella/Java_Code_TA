package com.javacode.java_code_ta.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "redis", 6379); // "redis" - container name
    }

}