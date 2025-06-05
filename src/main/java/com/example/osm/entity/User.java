package com.example.osm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"Users\"")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"FullName\"", nullable = false)
    private String fullName;

    @Column(name = "\"Age\"", nullable = false)
    private Short age;

    @OneToMany(mappedBy = "user")
    Set<Ticket> tickets = new LinkedHashSet<>();
}