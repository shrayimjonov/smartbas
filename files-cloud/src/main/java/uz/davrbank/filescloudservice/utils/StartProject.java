package uz.davrbank.filescloudservice.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartProject implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting filescloud service");
    }

}
