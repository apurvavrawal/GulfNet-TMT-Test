package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chat_notifications")
public class ChatNotification {
    @Id
    @Indexed(unique = true)
    private String id;

    private String senderId;
    private String receiverId;
    private String content;
}
