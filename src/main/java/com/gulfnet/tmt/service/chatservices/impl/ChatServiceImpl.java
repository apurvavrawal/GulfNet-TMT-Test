package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ChatDao;
import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ChatService;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatDao chatDao;
    private final ConversationService conversationService;
    private final ObjectMapper mapper;

    @Override
    public Chat savePrivateMessage(Chat chat) {
        var chatId = conversationService.getChatRoomId(chat,true);

        Chat newChat = new Chat();
        newChat.setConversationId(chatId);
        newChat.setContent(chat.getContent());
        newChat.setSenderId(chat.getSenderId());
        newChat.setSenderName(chat.getSenderName());
        newChat.setReceiverId(chat.getReceiverId());
        newChat.setReceiverName(chat.getReceiverName());
        Date currentDate = new Date();
        newChat.setDateCreated(currentDate);
        newChat.setAttachmentURL("Currently_No_Attachment");

        return chatDao.save(newChat);
    }

    @Override
    public ResponseDto<ChatResponse> getChatMessages(String conversationId, Pageable pageable) {
        Page<ChatResponse> chats = chatDao.findChatMessagesById(conversationId, pageable);
        return ResponseDto.<ChatResponse>builder()
                .data(chats.getContent())
                .count(chats.stream().count())
                .total(chats.getTotalElements())
                .build();
    }

    @Override
    public ResponseDto<ChatResponse> getMessageById(String chatId) {
        Chat chat = chatDao.findMessageById(chatId).orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Chat")));
        return ResponseDto.<ChatResponse>builder().status(0).data(List.of(mapper.convertValue(chat, ChatResponse.class))).build();
    }

    @Override
    public Chat saveGroupMessage(Chat chat) {
        var chatId = conversationService.getChatRoomId(chat,true);

        Chat newChat = new Chat();
        newChat.setConversationId(chatId);
        newChat.setContent(chat.getContent());
        newChat.setSenderId(chat.getSenderId());
        newChat.setSenderName(chat.getSenderName());
        newChat.setReceiverId(chat.getReceiverId());
        newChat.setReceiverName(chat.getReceiverName());
        Date currentDate = new Date();
        newChat.setDateCreated(currentDate);
        newChat.setAttachmentURL("Currently_No_Attachment");

        return chatDao.save(newChat);
    }
}
