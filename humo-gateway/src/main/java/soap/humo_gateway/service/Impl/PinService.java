package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.exception.PinCodeError;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;


@Service
@RequiredArgsConstructor
@Slf4j
public class PinService {

    private final HumoCredentials humoCredentials;

    private int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private char toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }


    private String xorHex(String a, String b) {
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars).toUpperCase();
    }


    public String generatePinBlock(String pin, String cardNumber) {
        if (pin.length() != 4) {
            throw new PinCodeError(String.format("Pin code format wrong, pin is: %s", pin));
        }
        //  as an alternative to below pinBlock formation, you can execute: String pinBlock = StringUtils.rightPad("0" + pin, 16, 'F');
        StringBuilder pinBlock = new StringBuilder(String.format("%s%d%s", "0", pin.length(), pin));
        while (pinBlock.length() != 16) {
            pinBlock.append("F");
        }

        int cardLen = cardNumber.length();
        String pan = "0000" + cardNumber.substring(cardLen - 13, cardLen - 1);
        return encryptData(xorHex(pinBlock.toString(), pan), humoCredentials.getZPK());
    }


    @SneakyThrows
    public String encryptData(String inputData, String keyData) {

        byte[] inputBytes = hexStringToByteArray(inputData);
        byte[] keyBytes = hexStringToByteArray(keyData);

        SecretKey key = new SecretKeySpec(keyBytes, "DESede");

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        return byteArrayToHexString(encryptedBytes);
    }


    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


}





