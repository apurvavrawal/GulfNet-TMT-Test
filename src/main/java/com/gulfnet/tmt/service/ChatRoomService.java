package com.gulfnet.tmt.service;

import com.gulfnet.tmt.entity.nosql.ChatRoom;
import com.gulfnet.tmt.repository.nosql.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(String senderId , String receiverId , boolean createNewRoomIfNotExists){

        return chatRoomRepository.findBySenderIdAndReceiverId(senderId,receiverId)
                .map(ChatRoom::getChatId)
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

        ChatRoom senderReceiver = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom receiverSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();

        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);

        return Optional.of(chatId);
    }
}
