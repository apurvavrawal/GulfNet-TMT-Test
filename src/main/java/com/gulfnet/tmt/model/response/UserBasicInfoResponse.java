package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserBasicInfoResponse {

    private UUID id;
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePhoto;
    private String status;

    public String getProfilePhoto() {
        return  ImageUtil.getB64EncodedStringFromImagePathOrURL(profilePhoto);
    }

}
