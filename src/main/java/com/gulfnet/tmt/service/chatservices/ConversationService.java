package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;

public interface ConversationService {
    ResponseDto<ConversationResponse> getConversationList(String userId);
}
