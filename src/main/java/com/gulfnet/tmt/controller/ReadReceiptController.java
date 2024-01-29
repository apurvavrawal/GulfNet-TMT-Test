package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.ReadReceipt;
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

    /*
     * In case of private chat the list of response contains one record and it's isRead field
     * can be checked.
     *
     * In case of group chat when all the consumers read the message then only we can consider this
     * as read for isRead field.
     * */

    @GetMapping("/status/{chatId}")
    public ResponseDto<ReadStatusResponse> getStatusForMessageRead(@PathVariable String chatId){
        log.info("Request received for getting read status for message: {}", chatId);

        List<ReadStatusResponse> readStatusResponses = readReceiptService.getStatusForMessage(chatId);
        if (readStatusResponses == null) {
            return ResponseDto.<ReadStatusResponse>builder()
                    .status(404)
                    .message("Message not found")
                    .build();
        } else {
            return ResponseDto.<ReadStatusResponse>builder()
                    .status(200)
                    .data(readStatusResponses)
                    .build();
        }
    }

    @GetMapping("/unread-count/{conversationId}")
    public long getUnreadMessageCount(@PathVariable("conversationId") String conversationId){
        log.info("Request received for get unread message count for conversation Id: {}", conversationId);
        return readReceiptService.getUnreadMessageCount(conversationId);
    }
}
