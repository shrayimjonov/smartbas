package uz.davrbank.esudservice.service.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.esudservice.aggregate.dto.reuqest.RegisterCaseRequestDto;
import uz.davrbank.esudservice.service.crypto.dto.CertificateWithKeyPair;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Service class responsible for cryptographic operations, including:
 * <ul>
 *     <li>Concatenation of parameters for signing or verification</li>
 *     <li>Loading certificates and key pairs from PFX files</li>
 *     <li>Signing data with a private key</li>
 *     <li>Verifying digital signatures</li>
 *     <li>Converting X509 certificates to Base64 format</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {

    /**
     * Concatenates important parameters from a {@link RegisterCaseRequestDto} into a single comma-separated string.
     * This string can be used for creating digital signatures or hashing.
     *
     * @param registration the registration request containing case and claim data
     * @return a comma-separated string of key parameters
     */
    public String getConcatenatedParameters(RegisterCaseRequestDto registration) {
        Set<String> parameters = new HashSet<>();
        parameters.add(registration.getElectronCaseId().toString());
        parameters.add(registration.getCaseDto().getDocNumber());
        parameters.add(registration.getCaseDto().getCourtId().toString());
        parameters.add(registration.getClaim().getClaimType().name());
        parameters.add(registration.getClaim().getClaimKind().name());
        parameters.add(registration.getParticipants() != null ? String.valueOf(registration.getParticipants().size()) : "0");
        parameters.add(registration.getReceipts() != null ? String.valueOf(registration.getReceipts().size()) : "0");

        return String.join(",", parameters);
    }

    /**
     * Loads a certificate and key pair from a PKCS12 (.pfx) file.
     *
     * @param pfxPath  the file path to the PFX certificate
     * @param password the password to unlock the PFX file
     * @return a {@link CertificateWithKeyPair} containing the certificate and associated key pair, or null if not found
     * @throws Exception if there is an error reading the keystore or keys
     */
    @SneakyThrows
    public CertificateWithKeyPair loadFromPfx(final String pfxPath, final String password) {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxPath)) {
            keyStore.load(fis, password.toCharArray());
        }
        Enumeration<String> aliases = keyStore.aliases();
        if (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            return new CertificateWithKeyPair(certificate, new KeyPair(publicKey, privateKey), null);
        }
        return null;
    }

    /**
     * Verifies a digital signature against the provided data and certificate.
     *
     * @param data           the original data bytes
     * @param signatureBytes the signature bytes to verify
     * @param certificate    the X509 certificate containing the public key
     * @return true if the signature is valid, false otherwise
     */
    public boolean verifySignature(byte[] data, byte[] signatureBytes, X509Certificate certificate) {
        try {
            PublicKey publicKey = certificate.getPublicKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("crypto-service.verify-signature -> Could not find crypto algorithm:", e);
        } catch (InvalidKeyException e) {
            log.error("crypto-service.verify-signature -> Invalid key :", e);
        } catch (SignatureException e) {
            log.error("crypto-service.verify-signature -> Signature not valid");
        }
        return false;
    }

    /**
     * Signs the given data using the provided private key with SHA256withRSA algorithm.
     *
     * @param data       the data bytes to sign
     * @param privateKey the private key used for signing
     * @return the digital signature bytes, or null if an error occurs
     */
    public byte[] sign(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            log.error("crypto-service.sign -> Error signing the document", e);
            return null;
        }
    }

    /**
     * Converts an X509 certificate to its Base64-encoded string representation.
     *
     * @param certificate the X509 certificate to convert
     * @return a Base64-encoded string of the certificate
     * @throws Exception if an error occurs while encoding the certificate
     */
    @SneakyThrows
    public static String convertToBase64(X509Certificate certificate) {
        byte[] encoded = certificate.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

}
