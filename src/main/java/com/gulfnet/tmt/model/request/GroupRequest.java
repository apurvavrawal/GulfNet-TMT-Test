package com.gulfnet.tmt.model.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class GroupRequest {

    private String code;
    private String name;
    private String icon;

    @Builder.Default
    private String type = "BASIC";
}
