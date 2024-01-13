package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.entity.nosql.ChatRoom;
import com.gulfnet.tmt.entity.nosql.UserDetail;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    private String receiverId;
    private String conversationId;
    private String content;
    private Timestamp dateCreated;
}
