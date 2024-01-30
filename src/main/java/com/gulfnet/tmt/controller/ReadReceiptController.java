package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.response.ReadStatusResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ReadReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/read-receipt")
public class ReadReceiptController {
    private final ReadReceiptService readReceiptService;

    @GetMapping("/unread-count/")
    public long getUnreadMessageCount(@PathVariable("conversationId") String conversationId, @PathVariable("userId") String userId){
        log.info("Request received for get unread message count for conversation Id: {}", conversationId);
        return readReceiptService.getUnreadMessageCount(conversationId);
    }
    /*@GetMapping("/unread-count/")
    public long getUnreadMessageCount(@RequestParam(name = "conversationId", required = false) String conversationId,
                                      @RequestParam(name = "userId", required = false) String userId) {
        log.info("Request received for get unread message count for conversation Id: {}", conversationId);
        if (conversationId != null) {
            // If conversationId is provided, return unread message count for that conversation
            return readReceiptService.getUnreadMessageCountByConversationId(conversationId);
        } else if (userId != null) {
            // If userId is provided, return overall unread message count for the user
            return readReceiptService.getOverallUnreadMessageCountForUser(userId);
        } else {
            // Handle the case when neither conversationId nor userId is provided
            // You may choose to throw an exception, return a default value, or handle it based on your requirement
            throw new IllegalArgumentException("Either conversationId or userId must be provided.");
        }
    }*/

}
