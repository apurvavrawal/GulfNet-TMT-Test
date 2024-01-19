package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ConversationForPrivateResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String profilePhoto;
}
