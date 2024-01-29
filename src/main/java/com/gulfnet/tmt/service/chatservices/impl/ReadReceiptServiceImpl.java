package com.gulfnet.tmt.service.chatservices.impl;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import com.gulfnet.tmt.model.response.ReadStatusResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import com.gulfnet.tmt.repository.nosql.ReadReceiptRepository;
import com.gulfnet.tmt.service.chatservices.ReadReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadReceiptServiceImpl implements ReadReceiptService {

    private final ChatRepository chatRepository;
    private final ReadReceiptRepository readReceiptRepository;


    @Override
    public long getUnreadMessageCount(String conversationId) {
        long unreadCount = 0;
        Chat chat = chatRepository.findByConversationId(conversationId);
        List<ReadReceipt> readReceipt = readReceiptRepository.findByConsumerId(chat.getReceiverId());
        for(ReadReceipt receipt: readReceipt){
            if(!receipt.isRead()){
                unreadCount++;
            }
        }
        return unreadCount;
    }

    @Override
    public List<ReadStatusResponse> getStatusForMessage(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return null;
        }
        // Find read receipts for the message
        List<ReadReceipt> readReceipts = readReceiptRepository.findByChatId(chat.getId());

        // Create ReadStatusResponse list
        List<ReadStatusResponse> readStatusResponses = new ArrayList<>();
        for (ReadReceipt readReceipt : readReceipts) {
            ReadStatusResponse response = new ReadStatusResponse();
            response.setChatId(readReceipt.getChatId());
            response.setSenderId(chat.getSenderId());
            response.setConsumerId(readReceipt.getConsumerId());
            response.setContent(chat.getContent());
            response.setRead(readReceipt.getSeenAt() != null);
            readStatusResponses.add(response);
        }
        return readStatusResponses;
    }

    @Override
    public void markMessageAsRead(String chatId, String consumerId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        Optional<ReadReceipt> existingReadReceipt = readReceiptRepository.findByChatIdAndConsumerId(chatId, consumerId);

        if(existingReadReceipt.isPresent())
        {
            ReadReceipt readReceipt = existingReadReceipt.get();
            readReceipt.setRead(true); // Mark message as read
            readReceipt.setSeenAt(new Date()); // Set the timestamp when message is read
            readReceiptRepository.save(readReceipt);
        }
        else{
            ReadReceipt newReadReceipt = new ReadReceipt();
            newReadReceipt.setChatId(chatId);
            newReadReceipt.setConsumerId(consumerId);
            newReadReceipt.setDeliveredAt(chat.get().getDateCreated());
            newReadReceipt.setSeenAt(new Date());
            newReadReceipt.setRead(false);
            readReceiptRepository.save(newReadReceipt);
        }
    }
}
