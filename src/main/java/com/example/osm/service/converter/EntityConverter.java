package com.example.osm.service.converter;

public interface EntityConverter<T1, T2>{
    T1 toEntity(T2 DTO);
    T2 toDTO(T1 entity);
}
