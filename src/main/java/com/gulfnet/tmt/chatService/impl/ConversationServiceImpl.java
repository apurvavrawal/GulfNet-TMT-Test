package com.gulfnet.tmt.chatService.impl;

import com.gulfnet.tmt.chatService.ConversationService;
import com.gulfnet.tmt.chatService.ReadReceiptService;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.*;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ConversationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ReadReceiptService readReceiptService;

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
        Conversation conversation = conversationRepository.findByConsumerId(chat.getReceiverId());
        if (conversation != null) {
            // update senderId as per the message send to the existing conversation of group.
            conversation.setSenderId(chat.getSenderId());
            conversationRepository.save(conversation);
            return conversation.getId();
        }else {
            return null;
        }
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

                Conversation savedConversation = conversationRepository.save(conversation);

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
    public ConversationResponse createConversationForGroup(ConversationRequest conversationRequest) {
        // check if conversation already present or not if not then create new
        Conversation conv = conversationRepository.findByConsumerId(conversationRequest.getConsumerId());
        ConversationResponse conversationResponse = new ConversationResponse();
        if(conv == null) {
                Conversation conversation = new Conversation();
                conversation.setConversationType(conversationRequest.getConversationType());
                conversation.setSenderId(conversationRequest.getSenderId());
                conversation.setConsumerId(conversationRequest.getConsumerId());

                Conversation savedConversation = conversationRepository.save(conversation);

                conversationResponse.setSenderId(conversationRequest.getSenderId());
                conversationResponse.setConsumerId(conversationRequest.getConsumerId());
                conversationResponse.setConversationId(savedConversation.getId());
                conversationResponse.setConversationType(savedConversation.getConversationType());
        }
        else{
            conversationResponse = setAlreadyExistConversation(conversationRequest);
        }
        return conversationResponse;
    }
    @Override
    public ResponseDto<ConversationListResponse> getConversationList(String userId, Pageable pageable) {
        List<ConversationListResponse> conversationListResponseList = getConversationList(userId)
                .stream()
                .map(conversation -> {
                    ConversationListResponse conversationListResponse = new ConversationListResponse();
                    try{
                        if(conversation.getId() != null) {
                            conversationListResponse.setConversationId(conversation.getId());
                            conversationListResponse.setConversationType(conversation.getConversationType());
                            if(conversation.getConversationType() == ConversationType.PRIVATE)
                            {
                                String requiredUser = Objects.equals(userId, conversation.getSenderId()) ?
                                        conversation.getConsumerId() :
                                        conversation.getSenderId();

                                User user = userDao.findUser(UUID.fromString(requiredUser))
                                        .orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE,
                                                MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
                                ConversationForPrivateResponse conversationForPrivateResponse = new ConversationForPrivateResponse();

                                conversationForPrivateResponse.setUserId(String.valueOf(user.getId()));
                                conversationForPrivateResponse.setUserName(user.getUserName());
                                conversationForPrivateResponse.setFirstName(user.getFirstName());
                                conversationForPrivateResponse.setLastName(user.getLastName());
                                conversationForPrivateResponse.setProfilePhoto(user.getProfilePhoto());

                                conversationListResponse.setConversationForPrivateResponse(conversationForPrivateResponse);
                                conversationListResponse.setUnReadMessageCount(readReceiptService.getUnreadMessageCount(conversation.getId(),String.valueOf(userId)));
                            }
                            if(conversation.getConversationType() == ConversationType.GROUP)
                            {
                                Group group = groupRepository.findById(UUID.fromString(conversation.getConsumerId()))
                                        .orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE,
                                                MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE,"Group")));

                                ConversationListForGroupResponse conversationListForGroupResponse = new ConversationListForGroupResponse();

                                conversationListForGroupResponse.setGroupId(String.valueOf(group.getId()));
                                conversationListForGroupResponse.setGroupName(group.getName());
                                conversationListForGroupResponse.setGroupIcon(group.getIcon());
                                conversationListForGroupResponse.setGroupCode(group.getCode());
                                conversationListForGroupResponse.setGroupType(group.getType());
                                conversationListResponse.setConversationListForGroupResponse(conversationListForGroupResponse);
                                conversationListResponse.setUnReadMessageCount(readReceiptService.getUnreadMessageCount(conversation.getId(),userId));
                            }
                            // Assign Last Message with each chat
                            Chat chat = chatRepository.findFirstByConversationIdOrderByDateCreatedDesc(conversation.getId());
                            conversationListResponse.setChat(chat);
                            return conversationListResponse;
                        }
                    }catch (NullPointerException ignored){}
                    return null;
                }).filter(Objects::nonNull)
                .toList();
        return ResponseDto.<ConversationListResponse>builder()
                .status(0)
                .data(conversationListResponseList)
                .count(conversationListResponseList.stream().count())
                .build();
    }

    public ConversationResponse setAlreadyExistConversation(ConversationRequest conversationRequest){
        ConversationResponse conversationResponse = new ConversationResponse();
        conversationResponse.setSenderId(conversationRequest.getSenderId());
        conversationResponse.setConsumerId(conversationRequest.getConsumerId());

        Conversation conversation = new Conversation();
        if(conversationRequest.getConversationType() == ConversationType.PRIVATE){
           conversation = conversationRepository.findBySenderIdAndConsumerIdAndConversationType(conversationRequest.getSenderId(), conversationRequest.getConsumerId(), conversationRequest.getConversationType());
        }
        if(conversationRequest.getConversationType() == ConversationType.GROUP){
            conversation = conversationRepository.findByConsumerIdAndConversationType(conversationRequest.getConsumerId(), conversationRequest.getConversationType());
        }
        conversationResponse.setConversationId(conversation.getId());
        conversationResponse.setConversationType(conversationRequest.getConversationType());
        return conversationResponse;
    }
    private List<Conversation> getConversationList(String userId) {
        List<Conversation> conversationForPrivateChat = new ArrayList<>();
        List<Conversation> conversation = conversationRepository.findBySenderIdOrConsumerId(userId,userId);
        for (Conversation conv: conversation){
            if(conv.getConversationType() == ConversationType.PRIVATE) {
                conversationForPrivateChat.add(conv);
            }
        }
        List<UserGroup> userGroupList = userGroupRepository.findAllByUserId(UUID.fromString(userId));
        List<Conversation> conversationForGroupChat = new ArrayList<>();
        for(UserGroup userGroup: userGroupList){
            Conversation newConversation = conversationRepository.findByConsumerId(String.valueOf(userGroup.getGroup().getId()));
            conversationForGroupChat.add(newConversation);
        }
        conversationForPrivateChat.addAll(conversationForGroupChat);
        return conversationForPrivateChat;
    }
}