package com.example.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaStreamApplication {


//
//	@Value("${spring.kafka.bootstrap-servers}")
//	private short bootstrapServer;

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamApplication.class, args);
		System.out.println("____");
	}

}
