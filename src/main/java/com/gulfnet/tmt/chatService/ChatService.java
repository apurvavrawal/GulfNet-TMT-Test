package com.gulfnet.tmt.chatService;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.GroupChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import org.springframework.data.domain.Pageable;


public interface ChatService {
    Chat savePrivateMessage(Chat chat);

    ResponseDto<ChatResponse> getChatMessagesForPrivate(String conversationId, Pageable pageable);

    Chat saveGroupMessage(Chat chat);

    ResponseDto<GroupChatResponse> getChatMessagesForGroup(String groupId, Pageable pageable);
}
