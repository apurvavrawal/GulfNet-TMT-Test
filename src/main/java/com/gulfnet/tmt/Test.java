package com.gulfnet.tmt;

import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.model.request.PasswordRequest;
import com.gulfnet.tmt.util.EncryptionUtil;
import io.swagger.v3.core.util.Json;

public class Test {
    public static void main(String[] args) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("ADMIN")
                .password("ADMIN")
                .machineInfo("test")
                .location("test")
                .appType("ADMIN")
                .build();

        PasswordRequest passwordRequest = PasswordRequest.builder()
                .userName("ADMIN")
                .currentPassword("ADMIN")
                .changePassword("Admin@123")
                .confirmPassword("Admin@123")
                .build();
        System.out.println(Json.pretty(passwordRequest));
        String encryptedText = EncryptionUtil.encrypt(Json.pretty(passwordRequest));
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}

