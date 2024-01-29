package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import com.gulfnet.tmt.model.response.ReadStatusResponse;
import com.gulfnet.tmt.model.response.ResponseDto;

import java.util.List;

public interface ReadReceiptService {
    ResponseDto<ReadReceipt> getUnreadMessageCount(String conversationId);

    List<ReadStatusResponse> getStatusForMessage(String chatId);
}
