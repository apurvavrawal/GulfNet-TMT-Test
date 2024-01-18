package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ConversationDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationForPrivateResponse;
import com.gulfnet.tmt.model.response.ConversationListResponse;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationDao conversationDao;
    private final UserDao userDao;
    private final ObjectMapper mapper;

    public Optional<String> getChatRoomId(String senderId , String receiverId , boolean createNewRoomIfNotExists){

        if(createNewRoomIfNotExists){
            return conversationRepository
                    .findBySenderIdAndConsumerId(senderId,receiverId)
                    .map(Conversation::getId);
        }
        else{
            ConversationRequest conversationRequest = new ConversationRequest(senderId, receiverId, ConversationType.PRIVATE);
            ConversationResponse conversationResponse = createConversation(conversationRequest);
            var newChatId = conversationResponse.getConversationId();
            return newChatId.describeConstable();
        }
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest conversationRequest) throws ValidationException{

        // check if conversation already present or not if not then create
        //Optional<Conversation> conv = conversationDao.getConversationBySenderAndConsumer(conversationRequest.getSenderId(), conversationRequest.getConsumerId());
        try {
            Optional<Conversation> conv = conversationRepository.findBySenderIdAndConsumerId(conversationRequest.getSenderId(),conversationRequest.getConsumerId());
            Conversation conversation = new Conversation();
            conversation.setConversationType(conversationRequest.getConversationType());
            conversation.setSenderId(conversationRequest.getSenderId());
            conversation.setConsumerId(conversationRequest.getConsumerId());
            // Save the conversation to the database
            Conversation savedConversation = conversationDao.createConversation(conversation);

            ConversationResponse conversationResponse = new ConversationResponse();

            conversationResponse.setSenderId(conversationRequest.getSenderId());
            conversationResponse.setConsumerId(conversationRequest.getConsumerId());
            conversationResponse.setConversationId(savedConversation.getId());
            conversationResponse.setConversationType(savedConversation.getConversationType());

            return conversationResponse;
        }
        catch (ValidationException e){
            ConversationResponse conversationResponse = new ConversationResponse();

            conversationResponse.setSenderId(conversationRequest.getSenderId());
            conversationResponse.setConsumerId(conversationRequest.getConsumerId());

            Conversation conversation = new Conversation();
            conversation = conversationRepository.findBySenderIdAndConsumerIdAndConversationType(conversationRequest.getSenderId(), conversationRequest.getConsumerId(), conversationRequest.getConversationType());
            conversationResponse.setConversationId(conversation.getId());
            conversationResponse.setConversationType(conversationRequest.getConversationType());

            return conversationResponse;

        }
    }

    @Override
    public ResponseDto<ConversationListResponse> getConversationList(String userId, Pageable pageable) {

        List<ConversationListResponse> conversationListResponseList = conversationDao.getConversationListForPrivate(userId)
                .stream()
                .map(conversation -> {
                    ConversationListResponse conversationListResponse = new ConversationListResponse();
                    conversationListResponse.setConversationId(conversation.getId());
                    conversationListResponse.setConversationType(conversation.getConversationType());

                    ConversationForPrivateResponse conversationForPrivateResponse = new ConversationForPrivateResponse();

                    String requiredUser = Objects.equals(userId, conversation.getSenderId()) ?
                            conversation.getConsumerId() :
                            conversation.getSenderId();

                    User user = userDao.findUser(UUID.fromString(requiredUser))
                            .orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE,
                                    MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));

                    conversationForPrivateResponse.setUserId(String.valueOf(user.getId()));
                    conversationForPrivateResponse.setFirstName(user.getFirstName());
                    conversationForPrivateResponse.setLastName(user.getLastName());
                    conversationForPrivateResponse.setProfilePhoto(user.getProfilePhoto());

                    conversationListResponse.setConversationForPrivateResponse(conversationForPrivateResponse);

                    return conversationListResponse;
                }).toList();
        return ResponseDto.<ConversationListResponse>builder().status(0).data(conversationListResponseList).build();
    }

}