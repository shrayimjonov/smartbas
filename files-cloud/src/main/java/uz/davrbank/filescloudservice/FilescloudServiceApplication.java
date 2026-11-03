package uz.davrbank.filescloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static uz.davrbank.filescloudservice.utils.IntilizationProject.loggingApplicationParams;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan(basePackages = "uz.davrbank.filescloudservice.*")
public class FilescloudServiceApplication {

    public static void main(String[] args) {
      var project =  SpringApplication.run(FilescloudServiceApplication.class, args);
        loggingApplicationParams(project.getEnvironment());
    }
}