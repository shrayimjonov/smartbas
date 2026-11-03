package uz.davrbank.educontrolservice.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class InitializationProject implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InitializationProject.class);
    Date date = new Date();
    String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
    String formattedDate = dateFormat.format(date);

    @Override
    public void run(String... args) {
        LOG.info("EDU CONTROL SERVICE STARTED!  {}", formattedDate);
    }


}