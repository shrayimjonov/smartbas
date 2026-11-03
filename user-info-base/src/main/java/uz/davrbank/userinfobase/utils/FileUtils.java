package uz.davrbank.userinfobase.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class FileUtils {

    public byte[] decodeBase64(String file) {

        if (file.contains(",")) {
            file = file.split(",")[1];
        }

        return Base64.getDecoder().decode(file);
    }
}