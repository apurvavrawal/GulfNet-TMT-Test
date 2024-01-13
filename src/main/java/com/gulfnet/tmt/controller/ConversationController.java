package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.response.ConversationResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("/{userId}")
    public ResponseDto<ConversationResponse> getConversationList(@PathVariable String userId){
        log.info("Received request for get conversation List for userId: {}",userId);
        return conversationService.getConversationList(userId);
    }

}
