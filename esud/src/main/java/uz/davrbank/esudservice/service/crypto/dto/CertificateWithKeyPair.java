package uz.davrbank.esudservice.service.crypto.dto;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

public record CertificateWithKeyPair(X509Certificate certificate,
                                     KeyPair keyPair,
                                     SecretKey secretKey) {
}
