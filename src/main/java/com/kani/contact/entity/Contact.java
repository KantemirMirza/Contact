package com.kani.contact.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Contact {
    @Id
    @UuidGenerator()
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    private String name;
    private String email;
    private String title;
    private String mobile;
    private String address;
    private String status;
    private String photoURL;
}
