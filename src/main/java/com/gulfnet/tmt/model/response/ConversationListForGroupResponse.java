package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
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
    private String groupCode;
    private String groupType;
    public String getGroupIcon() {
        return  ImageUtil.getB64EncodedStringFromImagePathOrURL(groupIcon);
    }
}
