package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import org.springframework.data.domain.Pageable;


public interface ChatService {
    Chat savePrivateMessage(Chat chat);

    ResponseDto<ChatResponse> getChatMessages(String conversationId, Pageable pageable);

    ResponseDto<ChatResponse> getMessageById(String chatId);

    Chat saveGroupMessage(Chat chat);
}
