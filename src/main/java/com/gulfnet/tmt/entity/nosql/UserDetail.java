package com.gulfnet.tmt.entity.nosql;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.util.enums.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_details")
public class UserDetail {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String profilePhoto;
    private ChatStatus chatStatus;

    @DBRef
    private User user;
}
