package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationListResponse;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ConversationService {
    Optional<String> getChatRoomId(String senderId, String receiverId, boolean b);
    ConversationResponse createConversation(ConversationRequest conversationRequest);

    ResponseDto<ConversationListResponse> getConversationList(String userId, Pageable pageable);
}
