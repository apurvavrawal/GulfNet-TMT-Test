package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ConversationListResponse {
    private String conversationId;
    private ConversationType conversationType;
    private ConversationForPrivateResponse conversationForPrivateResponse;
}
