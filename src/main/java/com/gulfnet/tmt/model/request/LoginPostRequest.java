package com.gulfnet.tmt.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class LoginPostRequest {

    @Schema(example = "(\\\"{\"userName\":\"USERNAME\",\"password\":\"12345\",\"appType\":\"ADMIN\",\"machineInfo\":\"Device Details\",\"location\":\"location of User\"}\")")
    String loginRequest;
}
