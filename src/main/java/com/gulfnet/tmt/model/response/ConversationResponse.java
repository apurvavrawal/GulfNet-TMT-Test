package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.enums.ConversationType;
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
    private String senderId ;
    private String receiverId;
    private ConversationType conversationType;
}
