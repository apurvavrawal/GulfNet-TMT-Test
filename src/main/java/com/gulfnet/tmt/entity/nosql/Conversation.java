package com.gulfnet.tmt.entity.nosql;

import com.gulfnet.tmt.entity.sql.Group;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "conversation")
@Builder
public class Conversation {
    @Id
    @Indexed(unique = true)
    private String id;

    @DBRef
    private UserDetail userDetail;
    private String userId;
    private String firstName;
    private String lastName;
    private String profilePhoto;

    @DBRef
    private Group group;
    private String groupId;

}
