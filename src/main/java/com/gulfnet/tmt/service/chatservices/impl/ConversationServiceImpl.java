package com.gulfnet.tmt.service.chatservices.impl;

import com.gulfnet.tmt.dao.ChatDao;
import com.gulfnet.tmt.dao.ConversationDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.*;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationDao conversationDao;
    private final UserDao userDao;
    private final UserGroupRepository userGroupRepository;
    private final ChatDao chatDao;

    public String getChatRoomId(Chat chat, boolean chatRoomExists) {

        // check from both sender and receiver side if conversation already present or not
        if (chatRoomExists) {
            Conversation conversation = conversationRepository.findBySenderIdAndConsumerId(chat.getSenderId(), chat.getReceiverId());
            if (conversation == null) {
                conversation = conversationRepository.findBySenderIdAndConsumerId(chat.getReceiverId(), chat.getSenderId());
            }
            return conversation.getId();
        } else {
            ConversationRequest conversationRequest = new ConversationRequest(chat.getSenderId(), chat.getReceiverId(), ConversationType.PRIVATE);
            ConversationResponse conversationResponse = createConversation(conversationRequest);
            var newChatId = conversationResponse.getConversationId();
            return newChatId;
        }
    }

    @Override
    public String getChatRoomIdForGroup(Chat chat) {
        // check from both sender and receiver side if conversation already present or not
        Conversation conversation = conversationRepository.findBySenderIdAndConsumerId(chat.getSenderId(), chat.getReceiverId());
        if (conversation == null) {
            conversation = conversationRepository.findBySenderIdAndConsumerId(chat.getReceiverId(), chat.getSenderId());
        }
        return conversation.getId();
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest conversationRequest) throws ValidationException{

        // check if conversation already present or not if not then create new
        Conversation conv = conversationRepository.findBySenderIdAndConsumerId(conversationRequest.getSenderId(),conversationRequest.getConsumerId());
        ConversationResponse conversationResponse = new ConversationResponse();
        if(conv == null){
            Conversation otherConv = conversationRepository.findBySenderIdAndConsumerId(conversationRequest.getConsumerId(),conversationRequest.getSenderId());
            if(otherConv == null) {
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

        List<ConversationListResponse> conversationListResponseList = conversationDao.getConversationList(userId)
                .stream()
                .map(conversation -> {
                    // Assign conversation details
                    ConversationListResponse conversationListResponse = new ConversationListResponse();
                    conversationListResponse.setConversationId(conversation.getId());
                    conversationListResponse.setConversationType(conversation.getConversationType());

                    String requiredUser = Objects.equals(userId, conversation.getSenderId()) ?
                            conversation.getConsumerId() :
                            conversation.getSenderId();

                    // Assign User details for Private Chat
                    User user = userDao.findUser(UUID.fromString(requiredUser))
                            .orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE,
                                    MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
                    ConversationForPrivateResponse conversationForPrivateResponse = new ConversationForPrivateResponse();

                    conversationForPrivateResponse.setUserId(String.valueOf(user.getId()));
                    conversationForPrivateResponse.setFirstName(user.getFirstName());
                    conversationForPrivateResponse.setLastName(user.getLastName());
                    conversationForPrivateResponse.setProfilePhoto(user.getProfilePhoto());

                    conversationListResponse.setConversationForPrivateResponse(conversationForPrivateResponse);

                    // Assign Group details for Group Chat
                    UserGroup userGroup = userGroupRepository.findById(UUID.fromString(requiredUser))
                            .orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE,
                                    MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE,"Group")));

                    ConversationListForGroupResponse conversationListForGroupResponse = new ConversationListForGroupResponse();

                    conversationListForGroupResponse.setGroupId(String.valueOf(userGroup.getGroup().getId()));
                    conversationListForGroupResponse.setGroupName(userGroup.getGroup().getName());
                    conversationListForGroupResponse.setGroupIcon(userGroup.getGroup().getIcon());

                    conversationListResponse.setConversationListForGroupResponse(conversationListForGroupResponse);

                    // Assign Last Message with each chat
                    Chat chat = chatDao.findLatestChatMessage(conversation.getId());
                    conversationListResponse.setChat(chat);

                    return conversationListResponse;
                }).toList();
        return ResponseDto.<ConversationListResponse>builder().status(0).data(conversationListResponseList).build();
    }

    public ConversationResponse setAlreadyExistConversation(ConversationRequest conversationRequest){
        ConversationResponse conversationResponse = new ConversationResponse();
        conversationResponse.setSenderId(conversationRequest.getSenderId());
        conversationResponse.setConsumerId(conversationRequest.getConsumerId());

        Conversation conversation = conversationRepository.findBySenderIdAndConsumerIdAndConversationType(conversationRequest.getSenderId(), conversationRequest.getConsumerId(), conversationRequest.getConversationType());
        conversationResponse.setConversationId(conversation.getId());
        conversationResponse.setConversationType(conversationRequest.getConversationType());
        return conversationResponse;
    }

}