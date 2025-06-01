package com.example.osm.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

public enum AirplaneStatus {
    IN_SERVICE,
    FLIGHT,
    LANDED,
    READY;
}