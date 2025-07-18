package com.example.osm.configs;

import com.example.osm.service.DTOConverter.DTOConverterFactory;
import com.example.osm.service.DTOConverter.EntityConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.util.List;

@Configuration
public class ConverterConfig {

    @Bean
    public DTOConverterFactory converterFactory(List<EntityConverter<?, ?>> converters) {
        DTOConverterFactory factory = new DTOConverterFactory();

        converters.forEach(converter -> {
            ResolvableType resolvableType = ResolvableType.forInstance(converter)
                    .as(EntityConverter.class);

            Class<?> entityClass = resolvableType.getGeneric(0).resolve();
            Class<?> dtoClass = resolvableType.getGeneric(1).resolve();

            if (entityClass != null && dtoClass != null) {
                registerConverter(factory, entityClass, dtoClass, converter);
            }
        });

        return factory;
    }

    @SuppressWarnings("unchecked")
    private <E, D> void registerConverter(
            DTOConverterFactory factory,
            Class<?> entityClass,
            Class<?> dtoClass,
            EntityConverter<?, ?> converter) {
        factory.registerConverters(
                (Class<E>) entityClass,
                (Class<D>) dtoClass,
                (EntityConverter<E, D>) converter
        );
    }
}
