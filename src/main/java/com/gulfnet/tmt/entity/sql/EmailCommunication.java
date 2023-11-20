package com.gulfnet.tmt.entity.sql;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Entity
public class EmailCommunication {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID")
    private UUID id;
    private String action;
    private String emailStatus;

}
