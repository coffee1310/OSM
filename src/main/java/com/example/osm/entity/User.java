package com.example.osm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"Users\"")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "\"FullName\"", nullable = false)
    private String fullName;

    @Column(name = "\"Age\"", nullable = false)
    private Short age;

}