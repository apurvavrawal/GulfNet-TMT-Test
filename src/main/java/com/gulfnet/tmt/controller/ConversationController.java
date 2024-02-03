package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.response.ConversationListResponse;
import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.chatService.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    // create new Conversation for Private chat
    @PostMapping(path = "/conversation/",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ConversationResponse createConversation(ConversationRequest conversationRequest){
        log.info("Received request to initiate conversation");
        return conversationService.createConversation(conversationRequest);
    }

    // get conversation list for chats both private and group
    @GetMapping("/conversation/{userId}")
    public ResponseDto<ConversationListResponse> getConversationList(@PathVariable String userId, Pageable pageable ){
        log.info("Received request for get conversation List for userId: {}",userId);
        return conversationService.getConversationList(userId, pageable);
    }
}
