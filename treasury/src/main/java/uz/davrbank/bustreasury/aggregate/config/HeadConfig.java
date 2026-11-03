package uz.davrbank.bustreasury.aggregate.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uz.davrbank.bustreasury.service.interfaces.AuthTokenInterface;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Component
public class HeadConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 693771187573065812L;
    private final AuthTokenInterface authTokenInterface;

    public HeadConfig(AuthTokenInterface authTokenInterface) {
        this.authTokenInterface = authTokenInterface;
    }

    public HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        try {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authTokenInterface.getAuthToken().getAccessToken());
        } catch (Exception e) {
            // Token olishda xato bo‘lsa, log qilamiz va xato holatini qaytarishimiz mumkin
            System.err.println("Failed to get auth token: " + e.getMessage());
        }
        return headers;
    }
}
