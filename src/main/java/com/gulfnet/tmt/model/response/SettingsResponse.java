package com.gulfnet.tmt.model.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class SettingsResponse {
    private UUID id;
    private String organizationCode;
    private String organizationName;
    private String languagePreference;
    private String supportEmail;
    private String supportUrl;
    private String supportPhoneNumber;
}
