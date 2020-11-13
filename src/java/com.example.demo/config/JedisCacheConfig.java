package com.example.demo.config;

import com.example.demo.util.YamlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "cache")
@PropertySource(value = "classpath:cache-config.yml", factory = YamlPropertySourceFactory.class)
public class JedisCacheConfig {

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    @Value("${spring.redis.timeout_seconds:15}")
    private int timeoutSeconds;

    private Map < String, Integer > ttlInSec;

    public Map < String, Integer > getTtlInSec() {
        return this.ttlInSec;
    }

    public void setTtlInSec(Map < String, Integer > ttlInSec) {
        this.ttlInSec = ttlInSec;
    }

    private RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(10));
    }

    private Map < String, RedisCacheConfiguration > setupDefaultCache() {
        final Map < String, RedisCacheConfiguration > redisCacheConfigurationMap = new HashMap < > ();

        // Setup TTL for each cacheName defined in cache-config.yml
        for (Map.Entry cacheItem: ttlInSec.entrySet()) {
            final String cacheName = (String) cacheItem.getKey();
            final int ttlValue = (int) cacheItem.getValue();
            log.info("####Cache items###: key={} ttl={}", cacheName, ttlValue);

            redisCacheConfigurationMap.put(
                    cacheName,
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(ttlValue)));
        }
        return redisCacheConfigurationMap;
    }

    private JedisPoolConfig jedisPoolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(20);
        jedisPoolConfig.setMaxWaitMillis(3000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(120000);
        jedisPoolConfig.setNumTestsPerEvictionRun(-1);

        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().usePooling().poolConfig(jedisPoolConfig()).build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory());
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration(), setupDefaultCache());
        return cacheManager;
    }
}
