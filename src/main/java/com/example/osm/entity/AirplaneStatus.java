package com.example.osm.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Enumerated;

public enum AirplaneStatus {
    IN_SERVICE,
    FLIGHT,
    LANDED,
    READY;

    @JsonCreator
    public static AirplaneStatus fromString(String value) {
        return valueOf(value.toUpperCase());
    }

    // Для сериализации в JSON (отправка значений)
    @JsonValue
    public String toValue() {
        return this.name();
    }
}