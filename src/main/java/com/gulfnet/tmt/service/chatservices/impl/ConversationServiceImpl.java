package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ConversationDao;
import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationDao conversationDao;
    private final ObjectMapper mapper;
    @Override
    public ResponseDto<ConversationResponse> getConversationList(String userId) {
        Conversation conversation = conversationDao.getConversationListByUserId(userId).orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_CODE, "Conversation")));
        return ResponseDto.<ConversationResponse>builder().status(0).data(List.of(mapper.convertValue(conversation, ConversationResponse.class))).build();
    }
}
