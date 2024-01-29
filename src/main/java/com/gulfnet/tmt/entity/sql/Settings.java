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
@EntityListeners(AuditingEntityListener.class)
public class Settings {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String organizationCode;
    private String organizationName;
    private String languagePreference;
    private String supportEmail;
    private String supportUrl;
    private String supportPhoneNumber;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @LastModifiedBy
    private String updatedBy;

    @CreatedBy
    private String createdBy;
}
