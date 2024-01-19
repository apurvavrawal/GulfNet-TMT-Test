package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.enums.ChatStatus;
import lombok.*;

@AllArgsConstructor
@Data
@ToString
@Builder
@NoArgsConstructor
public class ChatUserResponse {
    private String userId;
    private String firstName;
    private String profilePhoto;
    private ChatStatus chatStatus;
}
