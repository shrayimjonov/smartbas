package uz.davrbank.userinfobase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
@Data
@Entity
public class AuthTokenDavrHub {

    @Id
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String accessToken;

    private Instant expiresAt;
}