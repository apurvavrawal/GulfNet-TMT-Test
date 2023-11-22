package com.gulfnet.tmt.model.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class GroupResponse {

    private String id;
    private String code;
    private String name;
    private String icon;
    private String type;
    private Date dateCreated;
    private Date dateUpdated;
    private String updatedBy;
    private String createdBy;
}
