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
        System.out.println(Json.pretty(loginRequest));
        String encryptedText = EncryptionUtil.encrypt(Json.pretty(loginRequest),"4995f5e3-0280-4e6a-ad40-917136cbb884");
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = EncryptionUtil.adminDecrypt(encryptedText,"4995f5e3-0280-4e6a-ad40-917136cbb884");
        System.out.println("Decrypted Text: " + decryptedText);

        encryptedText = EncryptionUtil.mobileEncrypt(Json.pretty(loginRequest),"4995f5e3-0280-4e6a-ad40-917136cbb884");
        System.out.println("Mobile Encrypted Text: " + encryptedText);

        decryptedText = EncryptionUtil.mobileDecrypt("o+6Bo1oomD9dR51soR6zBlELb/ULVyxPa1ECYNO8htieZmNeer7oQtkUERL81nH/ZtwwysdeFa8HlqPElW2SBmOTNyko15EAOyoebMel5t81rF/bhhPQNApW9lvM1tWYZUN2PrKxduEsN6kbG73B/g==","4995f5e3-0280-4e6a-ad40-917136cbb884");
        System.out.println("Mobile Decrypted Text: " + decryptedText);
    }
}

