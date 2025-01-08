package org.playground.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * For scanning packages outside org.playground.spring package, use scanBasePackages.
 */
//@SpringBootApplication(
//		scanBasePackages = {"org.playground.spring", "org.playground.util"}
//)
@SpringBootApplication
//@EnableAutoConfiguration
public class Application {
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<?, ?> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
