package com.hyyh.festa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
//@EnableScheduling 스케줄링 옵션
public class FestaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FestaApplication.class, args);
	}

}
