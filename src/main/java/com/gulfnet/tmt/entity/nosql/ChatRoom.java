package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "chat_room")
public class ChatRoom {
    @Id
    @Indexed(unique = true)
    private String id;

   // @DBRef
   // private Conversation conversation;
    private String chatId;

    private String senderId;
    private String receiverId;

}
