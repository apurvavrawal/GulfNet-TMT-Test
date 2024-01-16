package com.gulfnet.tmt.model.response;

import lombok.*;

import java.sql.Timestamp;

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
    private String chatId;
    private String content;
    private Timestamp dateCreated;
}
