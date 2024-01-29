package com.gulfnet.tmt.entity.nosql;

import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
}
