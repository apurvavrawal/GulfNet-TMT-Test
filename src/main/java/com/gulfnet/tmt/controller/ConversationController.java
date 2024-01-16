package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ConversationListService;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConversationController {
    private final ConversationListService conversationListService;
    private final ConversationService conversationService;

    // create new Conversation
    @PostMapping(path = "/conversation",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<ConversationResponse> createConversation(ConversationRequest conversationRequest){
        log.info("Received request to initiate conversation");
        return conversationService.createConversation(conversationRequest);
    }

    @GetMapping("/{userId}")
    public ResponseDto<ConversationResponse> getConversationList(@PathVariable String userId){
        log.info("Received request for get conversation List for userId: {}",userId);
        return conversationListService.getConversationList(userId);
    }
}
