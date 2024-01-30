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

    @GetMapping("/unread-count/{userId}")
    public long getUnreadMessageCount(@RequestParam(name = "conversationId", required = false) String conversationId,
                                      @PathVariable("userId") String userId){
        log.info("Request received for get unread message count for user Id: {}", userId);
        if (conversationId == null) {
            // If conversationId is not provided, return unread message count for that userId
            return readReceiptService.getUnreadMessageCountByUserId(userId);
        } else if (userId != null) {
            // If conversationId is provided, return overall unread message count for the user
            return readReceiptService.getUnreadMessageCount(conversationId, userId);
        } else {
            throw new IllegalArgumentException("Either conversationId or userId must be provided.");
        }
    }
}
