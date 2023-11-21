package com.gulfnet.tmt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class LoginRequest {

    private String userName;
    private String appType;
    private String password;
    private String machineInfo;
    private String location;
}
