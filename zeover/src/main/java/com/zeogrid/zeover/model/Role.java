package com.zeogrid.zeover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zeogrid.zeover.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;


    @JsonIgnore
    public String getNameAsString() {
        return name.name();
    }
}
