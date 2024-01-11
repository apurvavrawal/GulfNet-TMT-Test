package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Chat {
    @Id
    private String id;

    private String chatId;
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
}
