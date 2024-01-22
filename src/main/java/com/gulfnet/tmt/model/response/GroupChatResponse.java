package com.gulfnet.tmt.model.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class GroupChatResponse {
    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String senderProfilePhoto;
    private String conversationId;
    private String content;
    private Date dateCreated;
}
