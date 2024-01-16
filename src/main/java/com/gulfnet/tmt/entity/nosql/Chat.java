package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

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

    private String conversationId;

    private String senderId;
    private String senderName;

    private String receiverId;
    private String receiverName;

    private String content;
    private String attachmentURL;

    @CreatedDate
    private Date dateCreated;
    @LastModifiedDate
    private Date dateModified;

}
