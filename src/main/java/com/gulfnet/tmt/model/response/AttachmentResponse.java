package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class AttachmentResponse {

    private String fileName;
    private String fileType;
    private String fileLocation;
    private String attachmentType;

}
