package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class GroupResponse {

    private UUID id;
    private String code;
    private String name;
    private String type;
    private String icon;
    private Long userCount;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private String updatedBy;
    private String createdBy;

    public String getIcon() {
        return  ImageUtil.getB64EncodedStringFromImagePathOrURL(icon);
    }

}

