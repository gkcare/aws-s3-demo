package com.gk.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AwsS3DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsS3DemoApplication.class, args);
	}

}
