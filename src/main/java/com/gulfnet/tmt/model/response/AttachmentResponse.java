package com.gulfnet.tmt.model.response;
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
