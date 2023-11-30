package com.gulfnet.tmt;

import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.model.request.PasswordRequest;
import com.gulfnet.tmt.util.EncryptionUtil;
import io.swagger.v3.core.util.Json;

public class Test {
    public static void main(String[] args) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("Prati@123")
                .password("Prati@112")
                .machineInfo("test")
                .location("test")
                .appType("MOBILE")
                .build();

        PasswordRequest passwordRequest = PasswordRequest.builder()
                .userName("ADMIN")
                .currentPassword("ADMIN")
                .changePassword("Admin@123")
                .confirmPassword("Admin@123")
                .build();
        System.out.println(Json.pretty(loginRequest));
        String encryptedText = EncryptionUtil.encrypt(Json.pretty(loginRequest));
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = EncryptionUtil.decrypt("AAAAAAAAAAAAAAAAAAAAAFNTY/vOQktsmZVtG6dXjfA=");
        System.out.println("Decrypted Text: " + decryptedText);
    }
}

