package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ChatDao;
import com.gulfnet.tmt.dao.ConversationDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.*;
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
    private final ChatDao chatDao;

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

        // check if conversation already present or not if not then create new
        Optional<Conversation> conv = conversationRepository.findBySenderIdAndConsumerId(conversationRequest.getSenderId(),conversationRequest.getConsumerId());
        ConversationResponse conversationResponse = new ConversationResponse();
        if(conv.isEmpty()){
            Optional<Conversation> otherConv = conversationRepository.findBySenderIdAndConsumerId(conversationRequest.getConsumerId(),conversationRequest.getSenderId());
            if(otherConv.isEmpty()) {
                Conversation conversation = new Conversation();
                conversation.setConversationType(conversationRequest.getConversationType());
                conversation.setSenderId(conversationRequest.getSenderId());
                conversation.setConsumerId(conversationRequest.getConsumerId());

                // Save the conversation to the database
                Conversation savedConversation = conversationDao.createConversation(conversation);

                conversationResponse.setSenderId(conversationRequest.getSenderId());
                conversationResponse.setConsumerId(conversationRequest.getConsumerId());
                conversationResponse.setConversationId(savedConversation.getId());
                conversationResponse.setConversationType(savedConversation.getConversationType());
            }else{
                    conversationResponse = setAlreadyExistConversation(conversationRequest);
                 }
        }else{
            conversationResponse = setAlreadyExistConversation(conversationRequest);
        }
        return conversationResponse;
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


                    //ChatResponse chatResponse = chatDao.findLatestChatMessage(requiredUser);

                    return conversationListResponse;
                }).toList();
        return ResponseDto.<ConversationListResponse>builder().status(0).data(conversationListResponseList).build();
    }

    private ConversationResponse setAlreadyExistConversation(ConversationRequest conversationRequest){
        ConversationResponse conversationResponse = new ConversationResponse();
        conversationResponse.setSenderId(conversationRequest.getSenderId());
        conversationResponse.setConsumerId(conversationRequest.getConsumerId());

        Conversation conversation = conversationRepository.findBySenderIdAndConsumerIdAndConversationType(conversationRequest.getSenderId(), conversationRequest.getConsumerId(), conversationRequest.getConversationType());
        conversationResponse.setConversationId(conversation.getId());
        conversationResponse.setConversationType(conversationRequest.getConversationType());
        return conversationResponse;
    }

}