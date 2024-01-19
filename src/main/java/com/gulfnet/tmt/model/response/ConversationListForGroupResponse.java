package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ConversationListForGroupResponse {
    private String groupId;
    private String groupName;
    private String groupIcon;
}
