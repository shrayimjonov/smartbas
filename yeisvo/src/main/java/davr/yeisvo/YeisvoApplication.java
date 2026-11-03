package davr.yeisvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YeisvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeisvoApplication.class, args);
	}

}
