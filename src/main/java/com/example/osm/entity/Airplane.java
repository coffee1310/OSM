package com.example.osm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"Airplanes\"")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airplane_id", nullable = false)
    private Long id;

    @Column(name = "model", nullable = false, length = 128)
    private String model;

    @Column(name = "number", nullable = false, length = 16)
    private String number;

    @OneToMany(mappedBy = "airplane")
    private Set<Flight> flights = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "airplane_status", nullable = false)
    private AirplaneStatus status;
}