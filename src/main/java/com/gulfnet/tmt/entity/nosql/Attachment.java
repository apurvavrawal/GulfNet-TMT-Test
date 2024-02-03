package com.gulfnet.tmt.entity.nosql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "attachments")
public class Attachment {

    @Id
    @Indexed(unique=true)
    private Integer fileId;
    private String fileName;
    private String fileType;
    private String fileLocation;
    private String attachmentType;
}
