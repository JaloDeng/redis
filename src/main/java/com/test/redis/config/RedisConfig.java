package com.test.redis.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(600))
				.disableCachingNullValues();

		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(redisCacheConfiguration)
				.transactionAware()
				.build();
	}
	
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		
		redisTemplate.afterPropertiesSet();
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		
		return redisTemplate;
	}

}
