package com.gulfnet.tmt.service.chatservices;

public interface ReadReceiptService {
    long getUnreadMessageCount(String conversationId, String userId);
    long getUnreadMessageCountByUserId(String userId);
}
