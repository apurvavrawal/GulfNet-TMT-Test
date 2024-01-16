package com.gulfnet.tmt.model.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ChatResponse {

    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String conversationId;
    private String content;
    private Date dateCreated;
}
