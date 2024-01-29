package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ConversationForPrivateResponse {
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String profilePhoto;
    public String getProfilePhoto() {
        return  ImageUtil.getB64EncodedStringFromImagePathOrURL(profilePhoto);
    }
}
