package com.gulfnet.tmt.model.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String email;
    private String profilePhoto;
    private String languagePreference;
    private List<String> userRole;
    private Date dateCreated;
    private Date dateUpdated;
    private String updatedBy;
    private String createdBy;
}
