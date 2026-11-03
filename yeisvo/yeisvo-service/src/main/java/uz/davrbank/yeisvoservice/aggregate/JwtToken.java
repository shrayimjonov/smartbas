package uz.davrbank.yeisvoservice.aggregate;

import lombok.Data;

@Data
public class JwtToken {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String skope;
    private Long expires_in;


}
