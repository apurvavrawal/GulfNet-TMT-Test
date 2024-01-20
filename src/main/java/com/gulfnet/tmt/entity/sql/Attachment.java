package com.gulfnet.tmt.entity.sql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "attachment")
public class Attachment {

    @Id
    private Integer fileId;
    private String fileName;
    private String fileType;
    private String fileLocation;
    private String attachmentType;
}
