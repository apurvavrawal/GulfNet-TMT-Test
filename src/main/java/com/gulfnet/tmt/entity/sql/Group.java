package com.gulfnet.tmt.entity.sql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="ORG_GROUP")
@EntityListeners(AuditingEntityListener.class)
public class Group {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String code;
    private String name;
    private String type;
    private String icon;
    private String status;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    @LastModifiedBy
    private String updatedBy;

    @CreatedBy
    private String createdBy;
}
