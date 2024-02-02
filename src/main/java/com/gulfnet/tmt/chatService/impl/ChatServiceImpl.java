package com.gulfnet.tmt.chatService.impl;

import com.gulfnet.tmt.chatService.ConversationService;
import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.GroupChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.chatService.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Chat savePrivateMessage(Chat chat) {
        Chat newChat = new Chat();

        newChat.setConversationId(conversationService.getChatRoomId(chat,true));
        newChat.setContent(chat.getContent());
        newChat.setSenderId(chat.getSenderId());
        newChat.setSenderName(chat.getSenderName());
        newChat.setReceiverId(chat.getReceiverId());
        newChat.setReceiverName(chat.getReceiverName());
        newChat.setLatitude(chat.getLatitude());
        newChat.setLongitude(chat.getLongitude());
        Date currentDate = new Date();
        newChat.setDateCreated(currentDate);
        newChat.setAttachmentURL(chat.getAttachmentURL());

        return chatRepository.save(newChat);
    }

    @Override
    public Chat saveGroupMessage(Chat chat) {
        Chat newChat = new Chat();

        newChat.setConversationId(conversationService.getChatRoomIdForGroup(chat));
        newChat.setContent(chat.getContent());
        newChat.setSenderId(chat.getSenderId());
        newChat.setSenderName(chat.getSenderName());
        newChat.setReceiverId(chat.getReceiverId());
        Optional<Group> group = groupRepository.findById(UUID.fromString(chat.getReceiverId()));
        newChat.setReceiverName(group.get().getName());
        newChat.setLatitude(chat.getLatitude());
        newChat.setLongitude(chat.getLongitude());
        Date currentDate = new Date();
        newChat.setDateCreated(currentDate);
        newChat.setAttachmentURL(chat.getAttachmentURL());

        return chatRepository.save(newChat);
    }

    @Override
    public ResponseDto<GroupChatResponse> getChatMessagesForGroup(String groupId, Pageable pageable) {
        Page<GroupChatResponse> chats = chatRepository.findByReceiverId(groupId, pageable);
        for (GroupChatResponse groupChatResponse : chats.getContent()) {
            Optional<User> user = userRepository.findById(UUID.fromString(groupChatResponse.getSenderId()));
            groupChatResponse.setSenderProfilePhoto(user.get().getProfilePhoto());
        }
        return ResponseDto.<GroupChatResponse>builder()
                .data(chats.getContent())
                .count(chats.stream().count())
                .total(chats.getTotalElements())
                .build();
    }

    @Override
    public ResponseDto<ChatResponse> getChatMessagesForPrivate(String conversationId, Pageable pageable) {
        Page<ChatResponse> chats = chatRepository.findByConversationId(conversationId, pageable);
        return ResponseDto.<ChatResponse>builder()
                .data(chats.getContent())
                .count(chats.stream().count())
                .total(chats.getTotalElements())
                .build();
    }
}
