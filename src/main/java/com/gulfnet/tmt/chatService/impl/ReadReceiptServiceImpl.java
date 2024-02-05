package com.gulfnet.tmt.chatService.impl;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import com.gulfnet.tmt.repository.nosql.ReadReceiptRepository;
import com.gulfnet.tmt.chatService.ReadReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReadReceiptServiceImpl implements ReadReceiptService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ReadReceiptRepository readReceiptRepository;


    @Override
    public long getUnreadMessageCount(String conversationId, String userId) {
        long unreadCount = 0;
        List<ReadReceipt> readReceipts = readReceiptRepository.findByConsumerIdAndConversationId(userId,conversationId);
        for(ReadReceipt receipt: readReceipts){
            Optional<Chat> chat = chatRepository.findById(receipt.getChatId());
            if(!Objects.equals(chat.get().getSenderId(), receipt.getConsumerId())){
                if(!receipt.isRead()){
                    unreadCount++;
                }
            }
        }
        return unreadCount;
    }
}
