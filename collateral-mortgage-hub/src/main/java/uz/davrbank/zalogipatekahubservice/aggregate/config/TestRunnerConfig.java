package uz.davrbank.zalogipatekahubservice.aggregate.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunnerConfig implements CommandLineRunner {

    private final  DatabaseConnectionTestConfig databaseConnectionTestConfig;
    public TestRunnerConfig(DatabaseConnectionTestConfig databaseConnectionTestConfig) {
        this.databaseConnectionTestConfig = databaseConnectionTestConfig;
    }

    @Override
    public void run(String... args) {
        System.out.println("  Oracle Cluster Connection process ...");
        databaseConnectionTestConfig.testConnection();
    }
}
