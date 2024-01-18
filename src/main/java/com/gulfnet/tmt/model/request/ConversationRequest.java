package com.gulfnet.tmt.model.request;

import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
    private String senderId;
    private String consumerId;
    private ConversationType conversationType;
}
