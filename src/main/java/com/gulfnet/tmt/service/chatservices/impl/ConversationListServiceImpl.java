package com.gulfnet.tmt.service.chatservices.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.ConversationListDao;
import com.gulfnet.tmt.entity.nosql.ConversationList;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ConversationListService;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationListServiceImpl implements ConversationListService {

    private final ConversationListDao conversationListDao;
    private final ObjectMapper mapper;
    @Override
    public ResponseDto<ConversationResponse> getConversationList(String userId) {
        ConversationList conversationList = conversationListDao.getConversationListByUserId(userId).orElseThrow(()-> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "ConversationList")));
        return ResponseDto.<ConversationResponse>builder().status(0).data(List.of(mapper.convertValue(conversationList, ConversationResponse.class))).build();
    }
}
