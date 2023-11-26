package com.gulfnet.tmt.model.request;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserPostRequest {

    private String userName;

    private String firstName;

    private String lastName;

    private String password;

    private String phone;

    private String email;

    @JsonIgnore
    private MultipartFile file;

    @Builder.Default
    private String appType = "MOBILE";

    private String languagePreference;

    @JsonIgnore
    private List<String> userRole;

}
