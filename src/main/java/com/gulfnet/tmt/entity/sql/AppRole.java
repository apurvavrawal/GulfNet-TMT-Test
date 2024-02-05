package com.gulfnet.tmt.entity.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
public class AppRole {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID")
    private UUID id;
    private String code;
    private String name;
}
