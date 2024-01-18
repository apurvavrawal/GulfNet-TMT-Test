package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ConversationListForGroupResponse {
    private String GroupName;
    private String GroupIcon;
}
