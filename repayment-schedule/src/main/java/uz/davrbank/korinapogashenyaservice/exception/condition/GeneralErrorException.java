package uz.davrbank.korinapogashenyaservice.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 22.01.2025
 * Time: 17:30
 * Project: korinapogasheniya
 */
@StandardException
public class GeneralErrorException extends RuntimeException {
    public GeneralErrorException(String message) {
        super(message);
    }
}
