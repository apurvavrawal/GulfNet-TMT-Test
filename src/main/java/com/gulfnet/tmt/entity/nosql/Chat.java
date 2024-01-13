package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
@Builder
public class Chat {
    @Id
    @Indexed(unique=true)
    private String id;

    @DBRef
    private UserDetail sender; //sender_id
    private String senderId;

    @DBRef
    private UserDetail receiver; // receiver_id
    private String receiverId;

    @DBRef
    private ChatRoom chatRoom; // chat_id
    private String conversationId;


    private String content;
    private String attachmentURL;

    @CreatedDate
    private Date dateCreated;
    @LastModifiedDate
    private Date dateModified;
}
