package com.example.osm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"Flight\"")
public class Flight {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "start_place", nullable = false, length = Integer.MAX_VALUE)
    private String startPlace;

    @Column(name = "end_place", nullable = false, length = Integer.MAX_VALUE)
    private String endPlace;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

}