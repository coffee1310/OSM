package com.example.osm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;


@SpringBootApplication()
public class OsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(OsmApplication.class, args);
    }
}
