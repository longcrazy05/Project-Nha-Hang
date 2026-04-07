package com.ttcs.ttcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ttcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ttcsApplication.class, args);
	}

}
