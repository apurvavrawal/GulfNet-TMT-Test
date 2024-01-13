package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ChatDao;
import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.ChatRoomService;
import com.gulfnet.tmt.service.chatservices.ChatService;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatDao chatDao;
    private final ChatRoomService chatRoomService;
    private final ObjectMapper mapper;

    @Override
    public Chat save(Chat chatMessage) {
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(),true).orElseThrow();
        chatMessage.setId(chatId);
        chatDao.save(chatMessage);
        return chatMessage;
    }

    @Override
    public ResponseDto<ChatResponse> getChatMessages(String senderId, String receiverId, Pageable pageable) {
        Page<ChatResponse> chats = chatDao.findChatMessagesById(senderId, receiverId, pageable);
        return ResponseDto.<ChatResponse>builder()
                .data(chats.getContent())
                .count(chats.stream().count())
                .total(chats.getTotalElements())
                .build();
//        var chatId = chatRoomService.getChatRoomId(senderId, receiverId,false);
//        return chatId.map(chatRoomId -> chatDao.findChatMessagesById(chatRoomId)).orElse(new ArrayList<>());

    }

    @Override
    public ResponseDto<ChatResponse> getMessageById(String chatId) {
        Chat chat = chatDao.findMessageById(chatId).orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Chat")));
        return ResponseDto.<ChatResponse>builder().status(0).data(List.of(mapper.convertValue(chat, ChatResponse.class))).build();
    }
}
