package com.gulfnet.tmt.chatService;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationListResponse;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import org.springframework.data.domain.Pageable;



public interface ConversationService {
    String getChatRoomId(Chat chat, boolean b);
    ConversationResponse createConversation(ConversationRequest conversationRequest);

    ResponseDto<ConversationListResponse> getConversationList(String userId, Pageable pageable);

    String getChatRoomIdForGroup(Chat chat);
}
