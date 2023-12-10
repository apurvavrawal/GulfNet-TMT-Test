package com.gulfnet.tmt.model.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserContactsResponse {

    private UUID id;
    private UUID userId;
    private List<UserBasicInfoResponse> userContacts;

}
