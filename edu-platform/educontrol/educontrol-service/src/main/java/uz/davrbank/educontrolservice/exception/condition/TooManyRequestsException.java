package uz.davrbank.educontrolservice.exception.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class TooManyRequestsException extends RuntimeException {

    private static final long serialVersionUID = -2376386853765195316L;

    private Map<String, Object> meta;

    public TooManyRequestsException(String blockedTime) {
        super("You have exhausted your API Request Quota");
        this.meta = Map.of(
                "blocked_time", blockedTime,
                "unit", "second");
    }
}
