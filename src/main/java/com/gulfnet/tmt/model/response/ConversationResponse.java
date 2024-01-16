package com.gulfnet.tmt.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private String conversationId;
    private String chatUserId;
    private String firstName;
    private String lastName;
    private String groupName;
    private String profilePhoto;
}
