package com.gulfnet.tmt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PasswordRequest {

    private String userName;
    private String currentPassword;
    private String changePassword;
    private String confirmPassword;
    private Long otp;

}
