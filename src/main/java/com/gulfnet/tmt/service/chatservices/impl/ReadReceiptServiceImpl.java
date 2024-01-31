package com.gulfnet.tmt.service.chatservices.impl;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import com.gulfnet.tmt.repository.nosql.ReadReceiptRepository;
import com.gulfnet.tmt.service.chatservices.ReadReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadReceiptServiceImpl implements ReadReceiptService {

    private final ChatRepository chatRepository;
    private final ReadReceiptRepository readReceiptRepository;


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
