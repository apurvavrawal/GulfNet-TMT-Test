package com.gulfnet.tmt.model.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SettingsRequest {
    private String languagePreference;
    private String supportEmail;
    private String supportUrl;
    private String supportPhoneNumber;
}
