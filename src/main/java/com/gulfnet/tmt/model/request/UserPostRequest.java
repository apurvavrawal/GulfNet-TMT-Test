package com.gulfnet.tmt.model.request;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gulfnet.tmt.config.MultipartFileSerializer;
import com.gulfnet.tmt.util.enums.Status;
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

    private String dorakuUserCode;

    @JsonSerialize(using = MultipartFileSerializer.class)
    private MultipartFile profilePhoto;

    @Builder.Default
    private String appType = "MOBILE";

    @Builder.Default
    private String status = Status.ACTIVE.getValue();

    private String languagePreference;

    @JsonIgnore
    private List<String> userRole;

    @JsonIgnore
    private List<String> userGroup;

}
