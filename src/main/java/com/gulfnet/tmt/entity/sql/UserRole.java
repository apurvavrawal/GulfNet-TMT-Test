package com.gulfnet.tmt.entity.sql;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="user_role")
public class UserRole {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name="ROLE_ID")
    private AppRole role;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

}
