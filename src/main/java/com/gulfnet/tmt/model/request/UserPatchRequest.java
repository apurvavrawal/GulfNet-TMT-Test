package com.gulfnet.tmt.model.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserPatchRequest {

    @JsonIgnore
    private MultipartFile file;
    private String languagePreference;
}
