package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.entity.sql.UserRole;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserPostResponse {

    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String email;
    private String profilePhoto;
    private String languagePreference;

    private List<UserRole> userRole;
    private List<UserGroup> userGroups;

    private Date dateCreated;
    private Date dateUpdated;
    private String updatedBy;
    private String createdBy;
}
