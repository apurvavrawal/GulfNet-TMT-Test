package com.gulfnet.tmt.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Data {

    @Id
    private String id;
    private String name;
    private int age;

}
