package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "readReceipts")
@Builder
public class ReadReceipt {
    @Id
    @Indexed(unique=true)
    private String id;

    private String chatId;

    private String consumerId;

    private String conversationId;

    private boolean isRead;

    @CreatedDate
    private Date deliveredAt;

    private Date seenAt;
}
