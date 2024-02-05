package com.gulfnet.tmt.chatService;

public interface ReadReceiptService {
    long getUnreadMessageCount(String conversationId, String userId);
}
