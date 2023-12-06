package com.gulfnet.tmt.util;

import com.gulfnet.tmt.exceptions.GulfNetSecurityException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
public class EncryptionUtil {

    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "AES";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    public static String encrypt(String plainText, String password) {
        try {
            SecretKey secretKey = generateSecretKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] ivBytes = new byte[16]; // Initialization Vector (IV) should be random
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combinedBytes = new byte[ivBytes.length + encryptedBytes.length];

            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes);
        } catch (Exception e) {
            throw new GulfNetSecurityException(ErrorConstants.ENCRYPTION_ERROR_CODE, ErrorConstants.ENCRYPTION_ERROR_MESSAGE);
        }
    }
    public static String mobileEncrypt(String plainText, String password) {
        try {
            SecretKey secretKey = generateSecretKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] ivBytes = new byte[16]; // Initialization Vector (IV) should be random
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new GulfNetSecurityException(ErrorConstants.ENCRYPTION_ERROR_CODE, ErrorConstants.ENCRYPTION_ERROR_MESSAGE);
        }
    }

    public static String adminDecrypt(String encryptedText, String password)  {
        try {
            SecretKey secretKey = generateSecretKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] encryptedBytesWithIV = Base64.getDecoder().decode(encryptedText );
            byte[] ivBytes = new byte[16];
            byte[] encryptedBytes = new byte[encryptedBytesWithIV.length - ivBytes.length];

            System.arraycopy(encryptedBytesWithIV, 0, ivBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytesWithIV, ivBytes.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new GulfNetSecurityException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    public static String mobileDecrypt(String encryptedText, String password)  {
        try {
            SecretKey secretKey = generateSecretKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] encryptedBytesWithIV = Base64.getDecoder().decode(encryptedText );
            byte[] ivBytes = new byte[16];

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytesWithIV);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new GulfNetSecurityException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    private static SecretKey generateSecretKey(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), password.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_FACTORY_ALGORITHM);
    }

}
