package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ReadStatusResponse {
    private String chatId;
    private String senderId;
    private String consumerId;
    private String content;
    private boolean isRead;
}
