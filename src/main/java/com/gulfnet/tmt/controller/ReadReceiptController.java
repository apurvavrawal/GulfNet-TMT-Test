package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.service.chatservices.ReadReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/read-receipt")
public class ReadReceiptController {
    private final ReadReceiptService readReceiptService;

    @GetMapping("/unread-count/")
    public long getUnreadMessageCount(@RequestParam(name = "conversationId") String conversationId,
                                      @RequestParam(name = "userId") String userId){
        log.info("Request received for get unread message count for user Id: {}", userId);
        return readReceiptService.getUnreadMessageCount(conversationId, userId);
    }
}
