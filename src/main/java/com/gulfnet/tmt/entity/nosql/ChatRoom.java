package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document
public class ChatRoom {

    @Id
    private String id;
    private String conversationId;
    private String senderId;
    private String receiverId;

}
