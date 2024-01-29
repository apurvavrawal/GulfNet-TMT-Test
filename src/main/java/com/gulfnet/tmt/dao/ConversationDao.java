package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConversationDao {

    private final ConversationRepository conversationRepository;
    private final UserGroupRepository userGroupRepository;

    public List<Conversation> getConversationList(String userId) {

        //Collect all private conversations based on sender and consumer
        List<Conversation> conversationForPrivateChat = new ArrayList<>();
        List<Conversation> conversation = conversationRepository.findBySenderIdOrConsumerId(userId,userId);
        for (Conversation conv: conversation){
            if(conv.getConversationType() == ConversationType.PRIVATE) {
                conversationForPrivateChat.add(conv);
            }
        }

        //Collect all private conversations based on sender and consumer
        List<UserGroup> userGroupList = userGroupRepository.findAllByUserId(UUID.fromString(userId)); //3 values
        List<Conversation> conversationForGroupChat = new ArrayList<>();
        for(UserGroup userGroup: userGroupList){
            Conversation newConversation = conversationRepository.findByConsumerId(String.valueOf(userGroup.getGroup().getId()));
            conversationForGroupChat.add(newConversation);
        }
        conversationForPrivateChat.addAll(conversationForGroupChat);
        return conversationForPrivateChat;
    }

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
