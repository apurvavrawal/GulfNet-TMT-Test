package com.gulfnet.tmt.model.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class LoginRequest {

    private String userName;
    private String password;
    private String machineInfo;
    private String location;
}
