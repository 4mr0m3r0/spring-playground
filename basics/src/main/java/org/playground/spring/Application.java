package org.playground.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * For scanning packages outside org.playground.spring package, use scanBasePackages.
 */
//@SpringBootApplication(
//		scanBasePackages = {"org.playground.spring", "org.playground.util"}
//)
@SpringBootApplication
//@EnableAutoConfiguration
@EnableConfigurationProperties
public class Application {
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<?, ?> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Value("${configuration.projectName}") // Another way to inject properties.
	void setProjectName(String projectName) {
		System.out.println("setting project name " + projectName);
	}

	@Autowired
	void setEnvironment(Environment environment) { // Another way, using global object.
		System.out.println("setting environment: " + environment.getProperty("configuration.projectName"));
	}

	@Autowired
	void setConfigurationProjectProperties(ConfigurationProjectProperties cp) { // here we're injecting a POJO of properties.
		System.out.println("configurationProjectProperties: " + cp.getProjectName());
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

@Component
@ConfigurationProperties("configuration") // it specifies that we want to map all properties that are prefixed with configuration onto POJO properties, on this object.
class ConfigurationProjectProperties {
	private String projectName;
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}

