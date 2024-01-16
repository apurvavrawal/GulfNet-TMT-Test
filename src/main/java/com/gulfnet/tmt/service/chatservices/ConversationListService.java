package com.gulfnet.tmt.service.chatservices;

import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;

public interface ConversationListService {
    ResponseDto<ConversationResponse> getConversationList(String userId);
}
