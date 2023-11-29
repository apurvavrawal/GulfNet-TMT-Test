package com.gulfnet.tmt.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class PasswordPostRequest {

    @Schema(example = "ChangePassword API : (\\\"{\"userName\":\"USERNAME\",\"currentPassword\":\"12345\",\"changePassword\":\"changePassword\",\"confirmPassword\":\"confirmPassword\"}\") " +
                     "ResetPassword API : (\\\"{\"userName\":\"USERNAME\",\"otp\":\"12345\",\"changePassword\":\"changePassword\",\"confirmPassword\":\"confirmPassword\"}\")")
    String passwordRequest;
}
