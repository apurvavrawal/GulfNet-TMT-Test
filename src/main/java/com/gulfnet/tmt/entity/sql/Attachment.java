package com.gulfnet.tmt.entity.sql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Entity
//@Table(name = "attachment")
@Document(collection = "attachment")
public class Attachment {

    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fileId;
    private String fileName;
    private String fileType;
    private String fileLocation;
}
