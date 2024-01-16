package com.gulfnet.tmt.service.chatservices.impl;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    public Optional<String> getChatRoomId(String senderId , String receiverId , boolean createNewRoomIfNotExists){

        return conversationRepository.findBySenderIdAndReceiverId(senderId,receiverId)
                .map(Conversation::getChatId)
                .or(()-> {
                    if(createNewRoomIfNotExists){
                        var newChatId = createChatId(senderId, receiverId);
                        return newChatId;
                    }
                    return Optional.empty();
                });
    }

    private Optional<String> createChatId(String senderId, String receiverId) {
        var chatId = String.format("%s_%s", senderId, receiverId);

        Conversation senderReceiver = Conversation.builder()
                .chatId(chatId)
                .conversationType(ConversationType.ONE_TO_ONE)
                .build();

        conversationRepository.save(senderReceiver);

        return Optional.of(chatId);
    }
}
