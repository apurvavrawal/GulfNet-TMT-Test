package com.gulfnet.tmt.model.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserFilterRequest {

    private List<String> userGroups;
    private List<String> userRoles;
    private String status;

}
