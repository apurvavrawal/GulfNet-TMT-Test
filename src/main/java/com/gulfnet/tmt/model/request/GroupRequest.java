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
public class GroupRequest {

    private String code;
    private String name;
    @JsonIgnore
    private MultipartFile icon;

    @Builder.Default
    private String type = "BASIC";
}
