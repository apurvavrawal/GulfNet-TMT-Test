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
@Document(collection = "participant_list")
@Builder
public class ConversationList {
    @Id
    @Indexed(unique = true)
    private String id;

    private String userId;
    private String firstName;
    private String lastName;
    private String profilePhoto;

    private String groupId;
    private String groupName;

    private String unreadMessageCount;

}
