package com.example.osm.service.DTOConverter;


import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class DTOConverterFactory {
    private final Map<Pair<Class<?>, Class<?>>, EntityConverter<?, ?>> converters = new HashMap<>();

    public <E, D> void registerConverters(Class<E> entityClass, Class<D> dtoClass, EntityConverter<E, D> converter) {
        converters.put(Pair.of(entityClass, dtoClass), converter);
    }

    @SuppressWarnings("unchecked")
    public <E,D> EntityConverter<E,D> getConverter(Class<E> entityClass, Class<D> dtoClass) {
        return (EntityConverter<E, D>) converters.get(Pair.of(entityClass, dtoClass));
    }
}
