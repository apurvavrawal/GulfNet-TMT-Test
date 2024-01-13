package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "conversations")
public class ChatRoom {
    @Id
    @Indexed(unique = true)
    private String id;

    private String conversationId;
    private String senderId;
    private String receiverId;

}
