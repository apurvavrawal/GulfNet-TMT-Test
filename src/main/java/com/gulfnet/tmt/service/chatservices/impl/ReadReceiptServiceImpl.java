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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadReceiptServiceImpl implements ReadReceiptService {

    private final ChatRepository chatRepository;
    private final ReadReceiptRepository readReceiptRepository;

    @Override
    public ResponseDto<ReadReceipt> getUnreadMessageCount(String conversationId) {
        return null;
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
            response.setConsumerId(chat.getReceiverId());
            response.setContent(chat.getContent());
            response.setRead(readReceipt.getSeenAt() != null);
            readStatusResponses.add(response);
        }
        return readStatusResponses;
    }
}
