package org.playground.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * For scanning packages outside org.playground.spring package, use scanBasePackages.
 */
//@SpringBootApplication(
//		scanBasePackages = {"org.playground.spring", "org.playground.util"}
//)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
