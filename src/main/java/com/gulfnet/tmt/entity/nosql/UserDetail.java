package com.gulfnet.tmt.entity.nosql;

import com.gulfnet.tmt.util.enums.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private ChatStatus chatStatus;
}
