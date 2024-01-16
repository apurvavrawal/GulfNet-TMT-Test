package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;

import java.util.Optional;

public interface ConversationService {
    Optional<String> getChatRoomId(String senderId, String receiverId, boolean b);
    ResponseDto<ConversationResponse> createConversation(ConversationRequest conversationRequest);
}
