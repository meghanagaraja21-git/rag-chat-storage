package com.northbay.rag_chat_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
	    SystemMetricsAutoConfiguration.class,
	    org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration.class,
	    org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration.class
	})

public class RagChatStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagChatStorageApplication.class, args);
	}

}
