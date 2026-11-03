package uz.davrbank.yeisvoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import static uz.davrbank.yeisvoservice.utils.StartingPoint.loggingApplicationParams;

@SpringBootApplication()
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class YeisvoApplication {

	public static void main(String[] args) {
        var jumpEnv = SpringApplication.run(YeisvoApplication.class, args);
        loggingApplicationParams(jumpEnv.getEnvironment());
	}

}
