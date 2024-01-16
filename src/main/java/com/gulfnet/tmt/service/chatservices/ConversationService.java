package com.gulfnet.tmt.service.chatservices;

import java.util.Optional;

public interface ConversationService {
    Optional<String> getChatRoomId(String senderId, String receiverId, boolean b);
}
