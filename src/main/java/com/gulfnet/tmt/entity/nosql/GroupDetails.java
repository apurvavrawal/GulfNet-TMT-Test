package com.gulfnet.tmt.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "group_details")
public class GroupDetails {
    @Id
    private String id;
    private String groupName;
    private String type;
    private String icon;
}
