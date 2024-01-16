package com.gulfnet.tmt.entity.nosql;

import com.gulfnet.tmt.util.enums.ConversationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "conversation")
public class Conversation {
    @Id
    @Indexed(unique = true)
    private String id;

//    private String conversationId;
    private String senderId;
    private String receiverId;
    private ConversationType conversationType;

}
