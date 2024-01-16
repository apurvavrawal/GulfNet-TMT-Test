package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ConversationDao;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationDao conversationDao;
    private final ObjectMapper mapper;

    public Optional<String> getChatRoomId(String senderId , String receiverId , boolean createNewRoomIfNotExists){

        return conversationRepository.findBySenderIdAndReceiverId(senderId,receiverId)
                .map(Conversation::getId)
                .or(()-> {
                    if(createNewRoomIfNotExists){
                        var newChatId = createChatId(senderId, receiverId);
                        return newChatId;
                    }
                    return Optional.empty();
                });
    }

    @Override
    public ResponseDto<ConversationResponse> createConversation(ConversationRequest conversationRequest) {

        Conversation conversation = new Conversation();
//        conversation.setSenderId(conversationRequest.getSenderId());
//        conversation.setReceiverId(conversationRequest.getReceiverId());
        conversation.setConversationType(conversationRequest.getConversationType());

        // Save the conversation to the database
        Conversation savedConversation = conversationDao.createConversation(conversation);

        // Prepare and return the response
        ConversationResponse conversationResponse = new ConversationResponse();
        conversationResponse.setConversationId(savedConversation.getId());
//        conversationResponse.setSenderId(savedConversation.getSenderId());
//        conversationResponse.setReceiverId(savedConversation.getReceiverId());
        conversationResponse.setConversationType(savedConversation.getConversationType());

        return ResponseDto.<ConversationResponse>builder().status(0).data(List.of(mapper.convertValue(conversation, ConversationResponse.class))).build();
    }

private Optional<String> createChatId(String senderId, String receiverId) {
        var chatId = String.format("%s_%s", senderId, receiverId);

        Conversation senderReceiver = Conversation.builder()
                .id(chatId)
                .conversationType(ConversationType.ONE_TO_ONE)
                .build();

        conversationRepository.save(senderReceiver);

        return Optional.of(chatId);
    }
}
