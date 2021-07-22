package com.sedo.AxBitTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AxBitTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AxBitTestApplication.class, args);
	}

}
